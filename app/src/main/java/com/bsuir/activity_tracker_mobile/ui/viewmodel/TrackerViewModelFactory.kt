package com.bsuir.activity_tracker_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bsuir.activity_tracker_mobile.data.repository.InMemoryTrackerRepository

class TrackerViewModelFactory(
    private val repository: InMemoryTrackerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DashboardViewModel::class.java -> DashboardViewModel(repository) as T
            ActivityViewModel::class.java -> ActivityViewModel(repository) as T
            NutritionViewModel::class.java -> NutritionViewModel(repository) as T
            FinanceViewModel::class.java -> FinanceViewModel(repository) as T
            ProfileViewModel::class.java -> ProfileViewModel(repository) as T
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
