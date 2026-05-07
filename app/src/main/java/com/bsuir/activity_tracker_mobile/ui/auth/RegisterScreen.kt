package com.bsuir.activity_tracker_mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bsuir.activity_tracker_mobile.R

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onGoToLogin: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordInvalidCharsHint by remember { mutableStateOf(false) }

    val emailError = state.fieldErrors[AuthField.Email]
    val usernameError = state.fieldErrors[AuthField.Username]
    val firstNameError = state.fieldErrors[AuthField.FirstName]
    val lastNameError = state.fieldErrors[AuthField.LastName]
    val passwordError = state.fieldErrors[AuthField.Password]
    val confirmPasswordError = state.fieldErrors[AuthField.ConfirmPassword]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.auth_register_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.auth_email_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                if (emailError != null) Text(text = emailError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.auth_username_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = usernameError != null,
            supportingText = {
                if (usernameError != null) Text(text = usernameError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.auth_first_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = firstNameError != null,
            supportingText = {
                if (firstNameError != null) Text(text = firstNameError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.auth_last_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = lastNameError != null,
            supportingText = {
                if (lastNameError != null) Text(text = lastNameError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isAllowedPasswordChar() }
                passwordInvalidCharsHint = filtered != newValue
                password = filtered
            },
            label = { Text(stringResource(R.string.auth_password_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                when {
                    passwordError != null -> Text(text = passwordError, color = MaterialTheme.colorScheme.error)
                    passwordInvalidCharsHint -> Text(text = stringResource(R.string.validation_password_allowed_chars_hint))
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { newValue ->
                confirmPassword = newValue.filter { it.isAllowedPasswordChar() }
            },
            label = { Text(stringResource(R.string.auth_confirm_password_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            supportingText = {
                if (confirmPasswordError != null) {
                    Text(text = confirmPasswordError, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        if (state.formError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = state.formError ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.register(
                    email = email,
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    password = password,
                    confirmPassword = confirmPassword,
                    onSuccess = onSuccess
                )
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.height(18.dp))
            } else {
                Text(stringResource(R.string.auth_register_action))
            }
        }

        TextButton(onClick = onGoToLogin, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.auth_go_to_login))
        }
    }
}

private fun Char.isAllowedPasswordChar(): Boolean {
    return (this in 'a'..'z') || (this in 'A'..'Z') || (this in '0'..'9') || this == '_'
}

