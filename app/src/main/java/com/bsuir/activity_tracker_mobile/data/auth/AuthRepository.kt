package com.bsuir.activity_tracker_mobile.data.auth

import com.bsuir.activity_tracker_mobile.data.network.ApiError
import com.bsuir.activity_tracker_mobile.data.network.ApiException
import com.bsuir.activity_tracker_mobile.data.network.api.AuthApi
import com.bsuir.activity_tracker_mobile.data.network.dto.LoginRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.RefreshRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.RegisterRequestDto
import java.time.OffsetDateTime

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {
    fun getTokensOrNull(): AuthTokens? = tokenStore.getTokensOrNull()

    fun clearTokens() = tokenStore.clear()

    suspend fun login(email: String, password: String): Result<AuthTokens> {
        return runCatching {
            val res = authApi.login(LoginRequestDto(email = email, password = password))
            if (!res.isSuccessful) {
                val problem = ApiError.parseProblem(res.errorBody())
                throw ApiException(
                    code = res.code(),
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP ${res.code()}"
                )
            }
            val body = requireNotNull(res.body())
            val tokens = body.toAuthTokens()
            tokenStore.saveTokens(tokens)
            tokens
        }
    }

    suspend fun register(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String
    ): Result<AuthTokens> {
        return runCatching {
            val res = authApi.register(
                RegisterRequestDto(
                    email = email,
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    password = password
                )
            )
            if (!res.isSuccessful) {
                val problem = ApiError.parseProblem(res.errorBody())
                throw ApiException(
                    code = res.code(),
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP ${res.code()}"
                )
            }
            val body = requireNotNull(res.body())
            val tokens = body.toAuthTokens()
            tokenStore.saveTokens(tokens)
            tokens
        }
    }

    suspend fun refresh(refreshToken: String): Result<AuthTokens> {
        return runCatching {
            val res = authApi.refresh(RefreshRequestDto(refreshToken = refreshToken))
            if (!res.isSuccessful) {
                val problem = ApiError.parseProblem(res.errorBody())
                throw ApiException(
                    code = res.code(),
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP ${res.code()}"
                )
            }
            val body = requireNotNull(res.body())
            val tokens = body.toAuthTokens()
            tokenStore.saveTokens(tokens)
            tokens
        }
    }

    private fun com.bsuir.activity_tracker_mobile.data.network.dto.AuthResponseDto.toAuthTokens(): AuthTokens {
        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            accessTokenExpiresAt = OffsetDateTime.parse(accessTokenExpiresAt),
            refreshTokenExpiresAt = OffsetDateTime.parse(refreshTokenExpiresAt)
        )
    }
}

