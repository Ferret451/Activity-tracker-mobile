package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.NutritionUiState

@Composable
fun NutritionScreen(uiState: NutritionUiState) {
    SectionScreen(
        title = "Питание",
        subtitle = "Основа для дневника калорий, макроэлементов и контроля воды.",
        cards = listOf(
            "Лимит" to uiState.dailyLimit,
            "Съедено" to uiState.consumedCalories,
            "Белок" to uiState.protein,
            "Вода" to uiState.water
        ),
        hint = uiState.nutritionHint
    )
}
