package com.bsuir.activity_tracker_mobile.ui.onboarding

import android.content.Context
import com.bsuir.activity_tracker_mobile.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.activity_tracker_mobile.data.network.ApiException
import com.bsuir.activity_tracker_mobile.data.network.dto.UpsertProfileRequestDto
import com.bsuir.activity_tracker_mobile.data.network.dto.UserProfileResponseDto
import com.bsuir.activity_tracker_mobile.data.profile.ProfileRepository
import com.bsuir.activity_tracker_mobile.domain.usecase.profile.ProfileField
import com.bsuir.activity_tracker_mobile.domain.usecase.profile.ValidateProfileInputUseCase
import com.bsuir.activity_tracker_mobile.ui.common.UiError
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingState(
    val isLoading: Boolean = false,
    val formError: String? = null,
    val fieldErrors: Map<ProfileField, String> = emptyMap(),
    val profile: UserProfileResponseDto? = null
)

class OnboardingViewModel(
    private val profileRepository: ProfileRepository,
    private val validateProfileInputUseCase: ValidateProfileInputUseCase,
    private val appContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun loadProfile() {
        _state.value = _state.value.copy(isLoading = true, formError = null)
        viewModelScope.launch {
            val res = profileRepository.getProfile()
            _state.value = if (res.isSuccess) {
                _state.value.copy(isLoading = false, profile = res.getOrNull())
            } else {
                val parsed = parseProfileError(
                    throwable = res.exceptionOrNull(),
                    defaultPrefix = appContext.getString(R.string.profile_load_failed)
                )
                _state.value.copy(
                    isLoading = false,
                    formError = parsed.formError
                )
            }
        }
    }

    fun submitProfile(
        weightKg: String,
        heightCm: String,
        age: String,
        sex: String,
        activityLevel: String,
        onSuccess: () -> Unit
    ) {
        val (valid, errors) = validateProfileInputUseCase.validate(
            weightKg = weightKg,
            heightCm = heightCm,
            age = age
        )
        if (errors.isNotEmpty() || valid == null) {
            _state.value = _state.value.copy(isLoading = false, fieldErrors = errors, formError = null)
            return
        }

        val req = UpsertProfileRequestDto(
            weightKg = valid.weightKg,
            heightCm = valid.heightCm,
            age = valid.age,
            sex = sex,
            activityLevel = activityLevel
        )

        _state.value = _state.value.copy(isLoading = true, formError = null, fieldErrors = emptyMap())
        viewModelScope.launch {
            val res = profileRepository.upsertProfile(req)
            _state.value = if (res.isSuccess) {
                onSuccess()
                _state.value.copy(isLoading = false, profile = res.getOrNull())
            } else {
                val parsed = parseProfileError(
                    throwable = res.exceptionOrNull(),
                    defaultPrefix = appContext.getString(R.string.profile_save_failed)
                )
                _state.value.copy(
                    isLoading = false,
                    formError = parsed.formError,
                    fieldErrors = parsed.fieldErrors
                )
            }
        }
    }

    private data class ParsedProfileError(
        val formError: String?,
        val fieldErrors: Map<ProfileField, String>
    )

    private fun parseProfileError(throwable: Throwable?, defaultPrefix: String): ParsedProfileError {
        if (throwable is IOException) {
            return ParsedProfileError(
                formError = appContext.getString(R.string.error_server_try_later),
                fieldErrors = emptyMap()
            )
        }

        val api = throwable as? ApiException
        if (api != null && api.code >= 500) {
            return ParsedProfileError(
                formError = appContext.getString(R.string.error_server_try_later),
                fieldErrors = emptyMap()
            )
        }

        val fieldErrors = api?.problem?.errors
            ?.mapNotNull { (key, value) ->
                val field = key.toProfileFieldOrNull() ?: return@mapNotNull null
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

        return ParsedProfileError(
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

    private fun String.toProfileFieldOrNull(): ProfileField? {
        return when (this.trim().lowercase()) {
            "weightkg", "weight_kg", "weight" -> ProfileField.WeightKg
            "heightcm", "height_cm", "height" -> ProfileField.HeightCm
            "age" -> ProfileField.Age
            "sex" -> ProfileField.Sex
            "activitylevel", "activity_level", "activity" -> ProfileField.ActivityLevel
            else -> null
        }
    }
}

