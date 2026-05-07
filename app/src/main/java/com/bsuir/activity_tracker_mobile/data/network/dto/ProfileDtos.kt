package com.bsuir.activity_tracker_mobile.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponseDto(
    @SerializedName("weightKg") val weightKg: Double? = null,
    @SerializedName("heightCm") val heightCm: Int? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("sex") val sex: String? = null,
    @SerializedName("activityLevel") val activityLevel: String? = null
)

data class UpsertProfileRequestDto(
    @SerializedName("weightKg") val weightKg: Double,
    @SerializedName("heightCm") val heightCm: Int,
    @SerializedName("age") val age: Int,
    @SerializedName("sex") val sex: String,
    @SerializedName("activityLevel") val activityLevel: String
)

