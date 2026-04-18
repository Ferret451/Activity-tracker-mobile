package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bsuir.activity_tracker_mobile.domain.repository.FinanceRepository

data class FinanceUiState(
    val monthBudget: String = "",
    val spentAmount: String = "",
    val savingsAmount: String = "",
    val topCategory: String = "",
    val financeHint: String = ""
)

class FinanceViewModel(
    repository: FinanceRepository
) : ViewModel() {
    private val summary = repository.getFinanceSummary()

    val uiState = FinanceUiState(
        monthBudget = "${summary.monthBudget.toInt()} р.",
        spentAmount = "${summary.spentAmount.toInt()} р.",
        savingsAmount = "${summary.savingsAmount.toInt()} р.",
        topCategory = summary.topCategory,
        financeHint = summary.financeHint
    )
}
