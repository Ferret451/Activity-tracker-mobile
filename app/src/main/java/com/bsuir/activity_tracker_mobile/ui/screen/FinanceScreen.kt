package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.FinanceUiState

@Composable
fun FinanceScreen(uiState: FinanceUiState) {
    SectionScreen(
        title = "Финансы",
        subtitle = "Базовый модуль бюджета, расходов, накоплений и финансовых категорий.",
        cards = listOf(
            "Бюджет" to uiState.monthBudget,
            "Потрачено" to uiState.spentAmount,
            "Накоплено" to uiState.savingsAmount,
            "Категория" to uiState.topCategory
        ),
        hint = uiState.financeHint
    )
}
