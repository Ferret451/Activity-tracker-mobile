package com.bsuir.activity_tracker_mobile.domain.usecase.auth

import android.content.Context
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.auth.AuthField

class ValidateAuthInputUseCase(
    private val context: Context
) {
    fun validateLogin(email: String, password: String): Map<AuthField, String> {
        val errors = linkedMapOf<AuthField, String>()
        if (email.isBlank()) {
            errors[AuthField.Email] = context.getString(R.string.validation_email_required)
        } else if (!EMAIL_REGEX.matches(email.trim())) {
            errors[AuthField.Email] = context.getString(R.string.validation_email_invalid)
        }
        if (password.isBlank()) {
            errors[AuthField.Password] = context.getString(R.string.validation_password_required)
        }
        return errors
    }

    fun validateRegister(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        confirmPassword: String
    ): Map<AuthField, String> {
        val errors = linkedMapOf<AuthField, String>()
        if (email.isBlank()) {
            errors[AuthField.Email] = context.getString(R.string.validation_email_required)
        } else if (!EMAIL_REGEX.matches(email.trim())) {
            errors[AuthField.Email] = context.getString(R.string.validation_email_invalid)
        }
        if (username.isBlank()) errors[AuthField.Username] = context.getString(R.string.validation_username_required)
        if (firstName.isBlank()) errors[AuthField.FirstName] = context.getString(R.string.validation_first_name_required)
        if (lastName.isBlank()) errors[AuthField.LastName] = context.getString(R.string.validation_last_name_required)

        if (password.isBlank()) {
            errors[AuthField.Password] = context.getString(R.string.validation_password_required)
        } else if (password.length < 8) {
            errors[AuthField.Password] = context.getString(R.string.validation_password_min_len, 8)
        }

        if (confirmPassword.isBlank()) {
            errors[AuthField.ConfirmPassword] = context.getString(R.string.validation_confirm_password_required)
        } else if (password.isNotBlank() && confirmPassword != password) {
            errors[AuthField.ConfirmPassword] = context.getString(R.string.validation_passwords_mismatch)
        }

        return errors
    }

    private companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}

