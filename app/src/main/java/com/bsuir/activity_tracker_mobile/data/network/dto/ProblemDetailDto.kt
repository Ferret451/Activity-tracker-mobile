package com.bsuir.activity_tracker_mobile.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProblemDetailDto(
    @SerializedName("type") val type: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("detail") val detail: String? = null,
    @SerializedName("errors") val errors: Map<String, String>? = null
)

