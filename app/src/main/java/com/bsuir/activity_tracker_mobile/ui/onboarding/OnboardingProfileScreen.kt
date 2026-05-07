package com.bsuir.activity_tracker_mobile.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.domain.usecase.profile.ProfileField

@Composable
fun OnboardingProfileScreen(
    viewModel: OnboardingViewModel,
    onCompleted: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    var weightKg by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf(Sex.Male) }
    var activityLevel by remember { mutableStateOf(ActivityLevel.Moderate) }

    var sexDialogOpen by remember { mutableStateOf(false) }
    var activityDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.onboarding_profile_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_profile_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        val weightError = state.fieldErrors[ProfileField.WeightKg]
        OutlinedTextField(
            value = weightKg,
            onValueChange = { weightKg = it },
            label = { Text(stringResource(R.string.onboarding_weight_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = weightError != null,
            supportingText = {
                if (weightError != null) Text(text = weightError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        val heightError = state.fieldErrors[ProfileField.HeightCm]
        OutlinedTextField(
            value = heightCm,
            onValueChange = { heightCm = it },
            label = { Text(stringResource(R.string.onboarding_height_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = heightError != null,
            supportingText = {
                if (heightError != null) Text(text = heightError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        val ageError = state.fieldErrors[ProfileField.Age]
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text(stringResource(R.string.onboarding_age_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = ageError != null,
            supportingText = {
                if (ageError != null) Text(text = ageError, color = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        val sexError = state.fieldErrors[ProfileField.Sex]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()
                    sexDialogOpen = true
                }
        ) {
            OutlinedTextField(
                value = stringResource(sex.labelRes),
                onValueChange = {},
                label = { Text(stringResource(R.string.onboarding_sex_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                isError = sexError != null,
                supportingText = {
                    if (sexError != null) Text(text = sexError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        val activityError = state.fieldErrors[ProfileField.ActivityLevel]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()
                    activityDialogOpen = true
                }
        ) {
            OutlinedTextField(
                value = stringResource(activityLevel.labelRes),
                onValueChange = {},
                label = { Text(stringResource(R.string.onboarding_activity_level_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false,
                isError = activityError != null,
                supportingText = {
                    if (activityError != null) Text(text = activityError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) }
            )
        }

        if (state.formError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = state.formError ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.submitProfile(
                    weightKg = weightKg,
                    heightCm = heightCm,
                    age = age,
                    sex = sex.apiValue,
                    activityLevel = activityLevel.apiValue,
                    onSuccess = onCompleted
                )
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.height(18.dp))
            } else {
                Text(stringResource(R.string.action_continue))
            }
        }
    }

    if (sexDialogOpen) {
        ChoiceDialog(
            title = stringResource(R.string.onboarding_sex_dialog_title),
            options = Sex.entries.map { stringResource(it.labelRes) },
            selectedIndex = Sex.entries.indexOf(sex),
            onSelect = { idx ->
                sex = Sex.entries[idx]
                sexDialogOpen = false
            },
            onDismiss = { sexDialogOpen = false }
        )
    }

    if (activityDialogOpen) {
        ChoiceDialog(
            title = stringResource(R.string.onboarding_activity_dialog_title),
            options = ActivityLevel.entries.map { stringResource(it.labelRes) },
            selectedIndex = ActivityLevel.entries.indexOf(activityLevel),
            onSelect = { idx ->
                activityLevel = ActivityLevel.entries[idx]
                activityDialogOpen = false
            },
            onDismiss = { activityDialogOpen = false }
        )
    }
}

private enum class Sex(val apiValue: String, val labelRes: Int) {
    Male("MALE", R.string.sex_male),
    Female("FEMALE", R.string.sex_female)
}

private enum class ActivityLevel(val apiValue: String, val labelRes: Int) {
    Sedentary("SEDENTARY", R.string.activity_sedentary),
    Light("LIGHT", R.string.activity_light),
    Moderate("MODERATE", R.string.activity_moderate),
    High("HIGH", R.string.activity_high),
    VeryHigh("VERY_HIGH", R.string.activity_very_high)
}

@Composable
private fun ChoiceDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn {
                itemsIndexed(options) { idx, label ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable { onSelect(idx) }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        RadioButton(
                            selected = selectedIndex == idx,
                            onClick = { onSelect(idx) }
                        )
                        Text(
                            text = label,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

