package com.bsuir.activity_tracker_mobile.data.repository

import com.bsuir.activity_tracker_mobile.core.model.ActivitySummary
import com.bsuir.activity_tracker_mobile.core.model.DashboardSummary
import com.bsuir.activity_tracker_mobile.core.model.FinanceSummary
import com.bsuir.activity_tracker_mobile.core.model.NutritionSummary
import com.bsuir.activity_tracker_mobile.core.model.ProfileSummary
import com.bsuir.activity_tracker_mobile.domain.repository.ActivityRepository
import com.bsuir.activity_tracker_mobile.domain.repository.DashboardRepository
import com.bsuir.activity_tracker_mobile.domain.repository.FinanceRepository
import com.bsuir.activity_tracker_mobile.domain.repository.NutritionRepository
import com.bsuir.activity_tracker_mobile.domain.repository.ProfileRepository

class InMemoryTrackerRepository :
    DashboardRepository,
    ActivityRepository,
    NutritionRepository,
    FinanceRepository,
    ProfileRepository {

    override fun getDashboardSummary(): DashboardSummary {
        return DashboardSummary(
            greeting = "Добрый вечер, Сергей",
            todaySteps = 6840,
            caloriesConsumed = 1780,
            caloriesBurned = 540,
            balance = 1240.0,
            weeklyFocus = "Сделать 3 тренировки и удержать лимит питания"
        )
    }

    override fun getActivitySummary(): ActivitySummary {
        return ActivitySummary(
            dailyGoal = 10000,
            currentSteps = 6840,
            distanceKm = 4.9,
            activeMinutes = 58,
            workoutHint = "До цели осталось 3160 шагов. Хороший момент для вечерней прогулки."
        )
    }

    override fun getNutritionSummary(): NutritionSummary {
        return NutritionSummary(
            dailyLimit = 2200,
            consumedCalories = 1780,
            proteinGrams = 96,
            waterMl = 1850,
            nutritionHint = "По белку вы идете в графике. До лимита осталось 420 ккал."
        )
    }

    override fun getFinanceSummary(): FinanceSummary {
        return FinanceSummary(
            monthBudget = 1800.0,
            spentAmount = 560.0,
            savingsAmount = 320.0,
            topCategory = "Питание",
            financeHint = "Расходы держатся в норме. Основная категория месяца: Питание."
        )
    }

    override fun getProfileSummary(): ProfileSummary {
        return ProfileSummary(
            userName = "Сергей Габрусь",
            age = 22,
            weightKg = 74,
            target = "Поддерживать баланс активности, питания и бюджета",
            connectedSources = listOf("Шагомер смартфона", "Ручной ввод питания", "Ручной учет расходов")
        )
    }
}
