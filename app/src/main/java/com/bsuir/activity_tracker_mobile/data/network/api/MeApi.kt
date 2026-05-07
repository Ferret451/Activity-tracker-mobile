package com.bsuir.activity_tracker_mobile.data.network.api

import com.bsuir.activity_tracker_mobile.data.network.dto.UpsertProfileRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.UserProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface MeApi {
    @GET("/api/me/profile")
    suspend fun getProfile(): Response<UserProfileResponseDto>

    @PUT("/api/me/profile")
    suspend fun upsertProfile(@Body body: UpsertProfileRequestDto): Response<UserProfileResponseDto>
}

