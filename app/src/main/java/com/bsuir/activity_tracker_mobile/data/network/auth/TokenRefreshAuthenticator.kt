package com.bsuir.activity_tracker_mobile.data.network.auth

import com.bsuir.activity_tracker_mobile.data.auth.TokenStore
import com.bsuir.activity_tracker_mobile.data.network.api.AuthApi
import com.bsuir.activity_tracker_mobile.data.network.dto.RefreshRequestDto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit

class TokenRefreshAuthenticator(
    private val tokenStore: TokenStore,
    private val authRetrofit: Retrofit
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest = response.request
        val path = originalRequest.url.encodedPath

        // If auth endpoint failed with 401, don't try to refresh.
        if (path.startsWith("/api/auth/")) return null

        // Avoid infinite retry loops.
        if (responseCount(response) >= 2) return null

        val currentTokens = tokenStore.getTokensOrNull() ?: return null
        if (currentTokens.refreshToken.isBlank()) return null

        val newTokens = runBlocking {
            mutex.withLock {
                // If another request already refreshed tokens, reuse them.
                val latest = tokenStore.getTokensOrNull()
                if (latest != null && latest.accessToken != currentTokens.accessToken) {
                    return@withLock latest
                }

                val api = authRetrofit.create(AuthApi::class.java)
                val refreshed = api.refresh(RefreshRequestDto(refreshToken = currentTokens.refreshToken))
                if (!refreshed.isSuccessful) {
                    tokenStore.clear()
                    return@withLock null
                }
                val body = refreshed.body() ?: run {
                    tokenStore.clear()
                    return@withLock null
                }

                // Update store
                tokenStore.saveTokens(
                    com.bsuir.activity_tracker_mobile.data.auth.AuthTokens(
                        accessToken = body.accessToken,
                        refreshToken = body.refreshToken,
                        tokenType = body.tokenType,
                        accessTokenExpiresAt = java.time.OffsetDateTime.parse(body.accessTokenExpiresAt),
                        refreshTokenExpiresAt = java.time.OffsetDateTime.parse(body.refreshTokenExpiresAt)
                    )
                )

                tokenStore.getTokensOrNull()
            }
        } ?: return null

        return originalRequest.newBuilder()
            .header("Authorization", "${newTokens.tokenType} ${newTokens.accessToken}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

