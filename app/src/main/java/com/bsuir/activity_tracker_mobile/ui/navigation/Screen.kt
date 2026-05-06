package com.bsuir.activity_tracker_mobile.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.bsuir.activity_tracker_mobile.R

sealed class Screen(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Dashboard : Screen("dashboard", R.string.nav_dashboard, Icons.Filled.Home)
    data object Activity : Screen("activity", R.string.nav_activity, Icons.AutoMirrored.Filled.DirectionsRun)
    data object Nutrition : Screen("nutrition", R.string.nav_nutrition, Icons.Filled.Restaurant)
    data object Finance : Screen("finance", R.string.nav_finance, Icons.Filled.Payments)
    data object Profile : Screen("profile", R.string.nav_profile, Icons.Filled.AccountCircle)
}

val bottomNavigationScreens = listOf(
    Screen.Dashboard,
    Screen.Activity,
    Screen.Nutrition,
    Screen.Finance,
    Screen.Profile
)
