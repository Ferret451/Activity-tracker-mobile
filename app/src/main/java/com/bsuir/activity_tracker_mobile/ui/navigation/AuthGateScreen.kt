package com.bsuir.activity_tracker_mobile.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bsuir.activity_tracker_mobile.data.auth.AuthRepository
import com.bsuir.activity_tracker_mobile.data.network.ApiException
import com.bsuir.activity_tracker_mobile.data.network.dto.UserProfileResponseDto
import com.bsuir.activity_tracker_mobile.data.profile.ProfileRepository
import java.time.OffsetDateTime

@Composable
fun AuthGateScreen(
    authRepository: AuthRepository,
    profileRepository: ProfileRepository,
    onNavigateToLogin: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    LaunchedEffect(Unit) {
        val tokens = authRepository.getTokensOrNull()
        if (tokens == null) {
            onNavigateToLogin()
            return@LaunchedEffect
        }

        val now = OffsetDateTime.now()
        val accessValid = tokens.accessTokenExpiresAt.isAfter(now.plusSeconds(5))
        val refreshValid = tokens.refreshTokenExpiresAt.isAfter(now.plusSeconds(5))

        if (!accessValid) {
            if (!refreshValid) {
                authRepository.clearTokens()
                onNavigateToLogin()
                return@LaunchedEffect
            }
            val refreshed = authRepository.refresh(tokens.refreshToken)
            if (refreshed.isFailure) {
                authRepository.clearTokens()
                onNavigateToLogin()
                return@LaunchedEffect
            }
        }

        val profileRes = profileRepository.getProfile()
        if (profileRes.isFailure) {
            val ex = profileRes.exceptionOrNull()
            val api = ex as? ApiException
            val detail = api?.problem?.detail.orEmpty().lowercase()
            val isMissingProfile404 = api?.code == 404 && detail.contains("profile")

            if ((api?.code == 401 || api?.code == 403) || (api?.code == 404 && !isMissingProfile404)) {
                authRepository.clearTokens()
                onNavigateToLogin()
                return@LaunchedEffect
            }
        }

        val profile = if (profileRes.isSuccess) profileRes.getOrNull() else null
        if (profile == null || !isProfileComplete(profile)) {
            onNavigateToOnboarding()
        } else {
            onNavigateToMain()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

private fun isProfileComplete(p: UserProfileResponseDto): Boolean {
    return p.weightKg != null &&
        p.heightCm != null &&
        p.age != null &&
        !p.sex.isNullOrBlank() &&
        !p.activityLevel.isNullOrBlank()
}

