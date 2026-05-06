package com.bsuir.activity_tracker_mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : Screen("dashboard", "\u0413\u043B\u0430\u0432\u043D\u0430\u044F", Icons.Filled.Home)
    data object Activity : Screen("activity", "\u0410\u043A\u0442\u0438\u0432\u043D\u043E\u0441\u0442\u044C", Icons.AutoMirrored.Filled.DirectionsRun)
    data object Nutrition : Screen("nutrition", "\u041F\u0438\u0442\u0430\u043D\u0438\u0435", Icons.Filled.Restaurant)
    data object Finance : Screen("finance", "\u0424\u0438\u043D\u0430\u043D\u0441\u044B", Icons.Filled.Payments)
    data object Profile : Screen("profile", "\u041F\u0440\u043E\u0444\u0438\u043B\u044C", Icons.Filled.AccountCircle)
}

val bottomNavigationScreens = listOf(
    Screen.Dashboard,
    Screen.Activity,
    Screen.Nutrition,
    Screen.Finance,
    Screen.Profile
)
