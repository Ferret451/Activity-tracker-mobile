package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bsuir.activity_tracker_mobile.domain.repository.ProfileRepository

data class ProfileUiState(
    val userName: String = "",
    val age: String = "",
    val weight: String = "",
    val target: String = "",
    val sourcesCount: String = "",
    val connectedSources: String = ""
)

class ProfileViewModel(
    repository: ProfileRepository
) : ViewModel() {
    private val summary = repository.getProfileSummary()

    val uiState = ProfileUiState(
        userName = summary.userName,
        age = "${summary.age} лет",
        weight = "${summary.weightKg} кг",
        target = summary.target,
        sourcesCount = "${summary.connectedSources.size}",
        connectedSources = summary.connectedSources.joinToString()
    )
}
