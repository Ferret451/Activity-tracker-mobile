package com.bsuir.activity_tracker_mobile.domain.usecase.profile

import android.content.Context
import com.bsuir.activity_tracker_mobile.R

enum class ProfileField {
    WeightKg,
    HeightCm,
    Age,
    Sex,
    ActivityLevel
}

data class ValidProfileInput(
    val weightKg: Double,
    val heightCm: Int,
    val age: Int
)

class ValidateProfileInputUseCase(
    private val context: Context
) {
    fun validate(
        weightKg: String,
        heightCm: String,
        age: String
    ): Pair<ValidProfileInput?, Map<ProfileField, String>> {
        val errors = linkedMapOf<ProfileField, String>()

        val weight = weightKg.trim().toDoubleOrNull()
        if (weightKg.isBlank()) {
            errors[ProfileField.WeightKg] = context.getString(R.string.validation_weight_required)
        } else if (weight == null) {
            errors[ProfileField.WeightKg] = context.getString(R.string.validation_number_invalid)
        } else if (weight <= 0.0) {
            errors[ProfileField.WeightKg] = context.getString(R.string.validation_number_positive)
        }

        val height = heightCm.trim().toIntOrNull()
        if (heightCm.isBlank()) {
            errors[ProfileField.HeightCm] = context.getString(R.string.validation_height_required)
        } else if (height == null) {
            errors[ProfileField.HeightCm] = context.getString(R.string.validation_number_invalid)
        } else if (height <= 0) {
            errors[ProfileField.HeightCm] = context.getString(R.string.validation_number_positive)
        }

        val ageValue = age.trim().toIntOrNull()
        if (age.isBlank()) {
            errors[ProfileField.Age] = context.getString(R.string.validation_age_required)
        } else if (ageValue == null) {
            errors[ProfileField.Age] = context.getString(R.string.validation_number_invalid)
        } else if (ageValue <= 0) {
            errors[ProfileField.Age] = context.getString(R.string.validation_number_positive)
        }

        if (errors.isNotEmpty() || weight == null || height == null || ageValue == null) {
            return null to errors
        }

        return ValidProfileInput(
            weightKg = weight,
            heightCm = height,
            age = ageValue
        ) to emptyMap()
    }
}

