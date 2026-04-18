package com.bsuir.activity_tracker_mobile.core.model

data class DashboardSummary(
    val greeting: String,
    val todaySteps: Int,
    val caloriesConsumed: Int,
    val caloriesBurned: Int,
    val balance: Double,
    val weeklyFocus: String
)

data class ActivitySummary(
    val dailyGoal: Int,
    val currentSteps: Int,
    val distanceKm: Double,
    val activeMinutes: Int,
    val workoutHint: String
)

data class NutritionSummary(
    val dailyLimit: Int,
    val consumedCalories: Int,
    val proteinGrams: Int,
    val waterMl: Int,
    val nutritionHint: String
)

data class FinanceSummary(
    val monthBudget: Double,
    val spentAmount: Double,
    val savingsAmount: Double,
    val topCategory: String,
    val financeHint: String
)

data class ProfileSummary(
    val userName: String,
    val age: Int,
    val weightKg: Int,
    val target: String,
    val connectedSources: List<String>
)
