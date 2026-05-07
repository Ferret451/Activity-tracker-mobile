package com.bsuir.activity_tracker_mobile.data.auth

import java.time.OffsetDateTime

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val accessTokenExpiresAt: OffsetDateTime,
    val refreshTokenExpiresAt: OffsetDateTime
)

