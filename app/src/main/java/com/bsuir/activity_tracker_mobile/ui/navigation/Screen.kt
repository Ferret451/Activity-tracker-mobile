package com.bsuir.activity_tracker_mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : Screen("dashboard", "Главная", Icons.Filled.Home)
    data object Activity : Screen("activity", "Активность", Icons.Filled.DirectionsRun)
    data object Nutrition : Screen("nutrition", "Питание", Icons.Filled.Restaurant)
    data object Finance : Screen("finance", "Финансы", Icons.Filled.Payments)
    data object Profile : Screen("profile", "Профиль", Icons.Filled.AccountCircle)
}

val bottomNavigationScreens = listOf(
    Screen.Dashboard,
    Screen.Activity,
    Screen.Nutrition,
    Screen.Finance,
    Screen.Profile
)
