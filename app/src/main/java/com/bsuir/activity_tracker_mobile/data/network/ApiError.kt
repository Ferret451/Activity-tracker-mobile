package com.bsuir.activity_tracker_mobile.data.network

import com.bsuir.activity_tracker_mobile.data.network.dto.ProblemDetailDto
import com.google.gson.Gson
import okhttp3.ResponseBody

data class ApiException(
    val code: Int,
    val problem: ProblemDetailDto?,
    override val message: String
) : Exception(message)

object ApiError {
    private val gson: Gson = Gson()

    fun parseProblem(body: ResponseBody?): ProblemDetailDto? {
        if (body == null) return null
        return try {
            gson.fromJson(body.string(), ProblemDetailDto::class.java)
        } catch (_: Exception) {
            null
        }
    }
}

