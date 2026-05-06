package com.bsuir.activity_tracker_mobile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bsuir.activity_tracker_mobile.ui.navigation.BottomNavigationBar
import com.bsuir.activity_tracker_mobile.ui.navigation.Screen
import com.bsuir.activity_tracker_mobile.ui.screen.ActivityScreen
import com.bsuir.activity_tracker_mobile.ui.screen.DashboardScreen
import com.bsuir.activity_tracker_mobile.ui.screen.FinanceScreen
import com.bsuir.activity_tracker_mobile.ui.screen.NutritionScreen
import com.bsuir.activity_tracker_mobile.ui.screen.ProfileScreen

@Composable
fun ActivityTrackerApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.Activity.route) {
                ActivityScreen()
            }
            composable(Screen.Nutrition.route) {
                NutritionScreen()
            }
            composable(Screen.Finance.route) {
                FinanceScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
