package com.bsuir.activity_tracker_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.bsuir.activity_tracker_mobile.data.auth.AuthRepository
import com.bsuir.activity_tracker_mobile.domain.usecase.auth.ValidateAuthInputUseCase

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val validateAuthInputUseCase: ValidateAuthInputUseCase,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, validateAuthInputUseCase, appContext) as T
        }
        error("Unknown ViewModel class: ${modelClass.name}")
    }
}

