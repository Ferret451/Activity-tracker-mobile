package com.bsuir.activity_tracker_mobile.domain.repository

import com.bsuir.activity_tracker_mobile.core.model.ActivitySummary
import com.bsuir.activity_tracker_mobile.core.model.DashboardSummary
import com.bsuir.activity_tracker_mobile.core.model.FinanceSummary
import com.bsuir.activity_tracker_mobile.core.model.NutritionSummary
import com.bsuir.activity_tracker_mobile.core.model.ProfileSummary

interface DashboardRepository {
    fun getDashboardSummary(): DashboardSummary
}

interface ActivityRepository {
    fun getActivitySummary(): ActivitySummary
}

interface NutritionRepository {
    fun getNutritionSummary(): NutritionSummary
}

interface FinanceRepository {
    fun getFinanceSummary(): FinanceSummary
}

interface ProfileRepository {
    fun getProfileSummary(): ProfileSummary
}
