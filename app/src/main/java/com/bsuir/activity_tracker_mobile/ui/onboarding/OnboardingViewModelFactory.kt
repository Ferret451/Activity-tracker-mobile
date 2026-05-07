package com.bsuir.activity_tracker_mobile.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.bsuir.activity_tracker_mobile.data.profile.ProfileRepository
import com.bsuir.activity_tracker_mobile.domain.usecase.profile.ValidateProfileInputUseCase

class OnboardingViewModelFactory(
    private val profileRepository: ProfileRepository,
    private val validateProfileInputUseCase: ValidateProfileInputUseCase,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(profileRepository, validateProfileInputUseCase, appContext) as T
        }
        error("Unknown ViewModel class: ${modelClass.name}")
    }
}

