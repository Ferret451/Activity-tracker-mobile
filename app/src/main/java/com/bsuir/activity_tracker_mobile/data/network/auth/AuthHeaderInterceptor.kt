package com.bsuir.activity_tracker_mobile.data.network.auth

import com.bsuir.activity_tracker_mobile.data.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        // Never attach auth header for auth endpoints themselves.
        if (path.startsWith("/api/auth/")) {
            return chain.proceed(request)
        }

        val tokens = tokenStore.getTokensOrNull()
        if (tokens?.accessToken.isNullOrBlank()) return chain.proceed(request)

        val authRequest = request.newBuilder()
            .header("Authorization", "${tokens.tokenType} ${tokens.accessToken}")
            .build()

        return chain.proceed(authRequest)
    }
}

