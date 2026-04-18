package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.ActivityUiState

@Composable
fun ActivityScreen(uiState: ActivityUiState) {
    SectionScreen(
        title = "Физическая активность",
        subtitle = "Базовый экран для шагов, дистанции, времени движения и тренировок.",
        cards = listOf(
            "Цель" to uiState.dailyGoal,
            "Сделано" to uiState.currentSteps,
            "Дистанция" to uiState.distanceKm,
            "Активные минуты" to uiState.activeMinutes
        ),
        hint = uiState.workoutHint
    )
}
