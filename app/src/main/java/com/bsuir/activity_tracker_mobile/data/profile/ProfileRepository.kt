package com.bsuir.activity_tracker_mobile.data.profile

import com.bsuir.activity_tracker_mobile.data.network.ApiError
import com.bsuir.activity_tracker_mobile.data.network.ApiException
import com.bsuir.activity_tracker_mobile.data.network.api.MeApi
import com.bsuir.activity_tracker_mobile.data.network.dto.UpsertProfileRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.UserProfileResponseDto

class ProfileRepository(
    private val meApi: MeApi
) {
    suspend fun getProfile(): Result<UserProfileResponseDto?> {
        return runCatching {
            val res = meApi.getProfile()
            if (res.code() == 404) {
                val problem = ApiError.parseProblem(res.errorBody())
                val detail = (problem?.detail ?: problem?.title).orEmpty().lowercase()

                // Normal case: profile not created yet -> start onboarding.
                if (detail.contains("profile")) return@runCatching null

                // Edge case: token is valid but subject user is missing -> treat as auth invalid.
                throw ApiException(
                    code = 404,
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP 404"
                )
            }
            if (!res.isSuccessful) {
                val problem = ApiError.parseProblem(res.errorBody())
                throw ApiException(
                    code = res.code(),
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP ${res.code()}"
                )
            }
            res.body()
        }
    }

    suspend fun upsertProfile(req: UpsertProfileRequestDto): Result<UserProfileResponseDto> {
        return runCatching {
            val res = meApi.upsertProfile(req)
            if (!res.isSuccessful) {
                val problem = ApiError.parseProblem(res.errorBody())
                throw ApiException(
                    code = res.code(),
                    problem = problem,
                    message = problem?.detail ?: problem?.title ?: "HTTP ${res.code()}"
                )
            }
            requireNotNull(res.body())
        }
    }
}

