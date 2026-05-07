package com.bsuir.activity_tracker_mobile.data.network.api

import com.bsuir.activity_tracker_mobile.data.network.dto.AuthResponseDto
import com.bsuir.activity_tracker_mobile.data.network.dto.LoginRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.LogoutRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.RefreshRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequestDto): Response<AuthResponseDto>

    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequestDto): Response<AuthResponseDto>

    @POST("/api/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): Response<AuthResponseDto>

    @POST("/api/auth/logout")
    suspend fun logout(@Body body: LogoutRequestDto): Response<Unit>
}

