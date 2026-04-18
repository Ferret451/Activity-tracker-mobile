package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.DashboardUiState

@Composable
fun DashboardScreen(uiState: DashboardUiState) {
    SectionScreen(
        title = uiState.greeting,
        subtitle = "Единая панель для активности, питания и личного бюджета.",
        cards = listOf(
            "Шаги сегодня" to uiState.todaySteps,
            "Питание" to uiState.caloriesConsumed,
            "Сожжено" to uiState.caloriesBurned,
            "Баланс" to uiState.balance
        ),
        hint = uiState.weeklyFocus
    )
}
