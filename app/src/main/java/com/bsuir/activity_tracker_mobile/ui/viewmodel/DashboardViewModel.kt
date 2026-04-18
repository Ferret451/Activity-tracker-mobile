package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bsuir.activity_tracker_mobile.domain.repository.DashboardRepository

data class DashboardUiState(
    val greeting: String = "",
    val todaySteps: String = "",
    val caloriesConsumed: String = "",
    val caloriesBurned: String = "",
    val balance: String = "",
    val weeklyFocus: String = ""
)

class DashboardViewModel(
    repository: DashboardRepository
) : ViewModel() {
    private val summary = repository.getDashboardSummary()

    val uiState = DashboardUiState(
        greeting = summary.greeting,
        todaySteps = "${summary.todaySteps}",
        caloriesConsumed = "${summary.caloriesConsumed} ккал",
        caloriesBurned = "${summary.caloriesBurned} ккал",
        balance = "${summary.balance.toInt()} р.",
        weeklyFocus = summary.weeklyFocus
    )
}
