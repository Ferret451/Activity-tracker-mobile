package com.bsuir.activity_tracker_mobile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bsuir.activity_tracker_mobile.data.repository.InMemoryTrackerRepository
import com.bsuir.activity_tracker_mobile.ui.navigation.BottomNavigationBar
import com.bsuir.activity_tracker_mobile.ui.navigation.Screen
import com.bsuir.activity_tracker_mobile.ui.screen.ActivityScreen
import com.bsuir.activity_tracker_mobile.ui.screen.DashboardScreen
import com.bsuir.activity_tracker_mobile.ui.screen.FinanceScreen
import com.bsuir.activity_tracker_mobile.ui.screen.NutritionScreen
import com.bsuir.activity_tracker_mobile.ui.screen.ProfileScreen
import com.bsuir.activity_tracker_mobile.ui.viewmodel.ActivityViewModel
import com.bsuir.activity_tracker_mobile.ui.viewmodel.DashboardViewModel
import com.bsuir.activity_tracker_mobile.ui.viewmodel.FinanceViewModel
import com.bsuir.activity_tracker_mobile.ui.viewmodel.NutritionViewModel
import com.bsuir.activity_tracker_mobile.ui.viewmodel.ProfileViewModel
import com.bsuir.activity_tracker_mobile.ui.viewmodel.TrackerViewModelFactory

@Composable
fun ActivityTrackerApp(repository: InMemoryTrackerRepository) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val factory = TrackerViewModelFactory(repository)

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
                val viewModel: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(uiState = viewModel.uiState)
            }
            composable(Screen.Activity.route) {
                val viewModel: ActivityViewModel = viewModel(factory = factory)
                ActivityScreen(uiState = viewModel.uiState)
            }
            composable(Screen.Nutrition.route) {
                val viewModel: NutritionViewModel = viewModel(factory = factory)
                NutritionScreen(uiState = viewModel.uiState)
            }
            composable(Screen.Finance.route) {
                val viewModel: FinanceViewModel = viewModel(factory = factory)
                FinanceScreen(uiState = viewModel.uiState)
            }
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = viewModel(factory = factory)
                ProfileScreen(uiState = viewModel.uiState)
            }
        }
    }
}
