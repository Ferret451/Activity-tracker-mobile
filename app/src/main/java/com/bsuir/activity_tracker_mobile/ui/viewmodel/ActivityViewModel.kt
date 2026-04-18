package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bsuir.activity_tracker_mobile.domain.repository.ActivityRepository

data class ActivityUiState(
    val dailyGoal: String = "",
    val currentSteps: String = "",
    val distanceKm: String = "",
    val activeMinutes: String = "",
    val workoutHint: String = ""
)

class ActivityViewModel(
    repository: ActivityRepository
) : ViewModel() {
    private val summary = repository.getActivitySummary()

    val uiState = ActivityUiState(
        dailyGoal = "${summary.dailyGoal} шагов",
        currentSteps = "${summary.currentSteps} шагов",
        distanceKm = "${summary.distanceKm} км",
        activeMinutes = "${summary.activeMinutes} мин",
        workoutHint = summary.workoutHint
    )
}
