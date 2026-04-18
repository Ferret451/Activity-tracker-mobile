package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.ProfileUiState

@Composable
fun ProfileScreen(uiState: ProfileUiState) {
    SectionScreen(
        title = uiState.userName,
        subtitle = "Профиль пользователя и подключенные источники данных.",
        cards = listOf(
            "Возраст" to uiState.age,
            "Вес" to uiState.weight,
            "Цель" to uiState.target,
            "Источники" to uiState.sourcesCount
        ),
        hint = uiState.connectedSources
    )
}
