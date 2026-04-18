package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bsuir.activity_tracker_mobile.domain.repository.NutritionRepository

data class NutritionUiState(
    val dailyLimit: String = "",
    val consumedCalories: String = "",
    val protein: String = "",
    val water: String = "",
    val nutritionHint: String = ""
)

class NutritionViewModel(
    repository: NutritionRepository
) : ViewModel() {
    private val summary = repository.getNutritionSummary()

    val uiState = NutritionUiState(
        dailyLimit = "${summary.dailyLimit} ккал",
        consumedCalories = "${summary.consumedCalories} ккал",
        protein = "${summary.proteinGrams} г",
        water = "${summary.waterMl} мл",
        nutritionHint = summary.nutritionHint
    )
}
