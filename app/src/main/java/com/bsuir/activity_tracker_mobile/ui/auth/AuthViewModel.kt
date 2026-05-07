package com.bsuir.activity_tracker_mobile.ui.auth

import android.content.Context
import com.bsuir.activity_tracker_mobile.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.activity_tracker_mobile.data.auth.AuthRepository
import com.bsuir.activity_tracker_mobile.data.network.ApiException
import com.bsuir.activity_tracker_mobile.domain.usecase.auth.ValidateAuthInputUseCase
import com.bsuir.activity_tracker_mobile.ui.common.UiError
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthField {
    Email,
    Password,
    ConfirmPassword,
    Username,
    FirstName,
    LastName
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val formError: String? = null,
    val fieldErrors: Map<AuthField, String> = emptyMap()
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val validateAuthInputUseCase: ValidateAuthInputUseCase,
    private val appContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        val validation = validateAuthInputUseCase.validateLogin(email = email, password = password)
        if (validation.isNotEmpty()) {
            _state.value = AuthUiState(isLoading = false, fieldErrors = validation)
            return
        }

        _state.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.login(email = email, password = password)
            _state.value = if (result.isSuccess) {
                onSuccess()
                AuthUiState(isLoading = false)
            } else {
                val err = result.exceptionOrNull()
                val parsed = parseAuthError(err, defaultPrefix = appContext.getString(R.string.auth_login_failed))
                AuthUiState(isLoading = false, formError = parsed.formError, fieldErrors = parsed.fieldErrors)
            }
        }
    }

    fun register(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        val validation = validateAuthInputUseCase.validateRegister(
            email = email,
            username = username,
            firstName = firstName,
            lastName = lastName,
            password = password,
            confirmPassword = confirmPassword
        )
        if (validation.isNotEmpty()) {
            _state.value = AuthUiState(isLoading = false, fieldErrors = validation)
            return
        }

        _state.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.register(
                email = email,
                username = username,
                firstName = firstName,
                lastName = lastName,
                password = password
            )
            _state.value = if (result.isSuccess) {
                onSuccess()
                AuthUiState(isLoading = false)
            } else {
                val err = result.exceptionOrNull()
                val parsed = parseAuthError(err, defaultPrefix = appContext.getString(R.string.auth_register_failed))
                AuthUiState(isLoading = false, formError = parsed.formError, fieldErrors = parsed.fieldErrors)
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(formError = null, fieldErrors = emptyMap())
    }

    private data class ParsedAuthError(
        val formError: String?,
        val fieldErrors: Map<AuthField, String>
    )

    private fun parseAuthError(throwable: Throwable?, defaultPrefix: String): ParsedAuthError {
        if (throwable is IOException) {
            return ParsedAuthError(
                formError = appContext.getString(R.string.error_server_try_later),
                fieldErrors = emptyMap()
            )
        }

        val api = throwable as? ApiException
        if (api != null && api.code >= 500) {
            return ParsedAuthError(
                formError = appContext.getString(R.string.error_server_try_later),
                fieldErrors = emptyMap()
            )
        }

        val fieldErrors = api?.problem?.errors
            ?.mapNotNull { (key, value) ->
                val field = key.toAuthFieldOrNull() ?: return@mapNotNull null
                if (value.isBlank()) return@mapNotNull null
                field to value
            }
            ?.toMap()
            .orEmpty()

        val formError = when {
            api != null -> appContext.getString(
                R.string.error_http_code,
                api.code,
                httpCodeExplanation(api.code)
            )
            throwable != null -> UiError.toUserMessage(defaultPrefix, throwable)
            else -> defaultPrefix
        }

        return ParsedAuthError(
            formError = formError,
            fieldErrors = fieldErrors
        )
    }

    private fun httpCodeExplanation(code: Int): String {
        val resId = when (code) {
            400 -> R.string.http_400
            401 -> R.string.http_401
            403 -> R.string.http_403
            404 -> R.string.http_404
            409 -> R.string.http_409
            422 -> R.string.http_422
            429 -> R.string.http_429
            in 400..499 -> R.string.http_4xx
            in 500..599 -> R.string.http_5xx
            else -> R.string.http_unknown
        }
        return appContext.getString(resId)
    }

    private fun String.toAuthFieldOrNull(): AuthField? {
        return when (this.trim().lowercase()) {
            "email" -> AuthField.Email
            "password" -> AuthField.Password
            "confirmpassword", "confirm_password", "passwordconfirmation", "password_confirmation" -> AuthField.ConfirmPassword
            "username", "userName".lowercase() -> AuthField.Username
            "firstname", "first_name", "firstName".lowercase() -> AuthField.FirstName
            "lastname", "last_name", "lastName".lowercase() -> AuthField.LastName
            else -> null
        }
    }

    private companion object
}

