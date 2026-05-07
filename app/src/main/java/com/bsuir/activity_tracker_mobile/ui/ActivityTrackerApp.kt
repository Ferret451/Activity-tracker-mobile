package com.bsuir.activity_tracker_mobile.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bsuir.activity_tracker_mobile.core.ServiceLocator
import com.bsuir.activity_tracker_mobile.ui.auth.AuthViewModel
import com.bsuir.activity_tracker_mobile.ui.auth.AuthViewModelFactory
import com.bsuir.activity_tracker_mobile.ui.auth.LoginScreen
import com.bsuir.activity_tracker_mobile.ui.auth.RegisterScreen
import com.bsuir.activity_tracker_mobile.ui.navigation.BottomNavigationBar
import com.bsuir.activity_tracker_mobile.ui.navigation.Routes
import com.bsuir.activity_tracker_mobile.ui.navigation.Screen
import com.bsuir.activity_tracker_mobile.ui.navigation.AuthGateScreen
import com.bsuir.activity_tracker_mobile.ui.onboarding.OnboardingProfileScreen
import com.bsuir.activity_tracker_mobile.ui.onboarding.OnboardingViewModel
import com.bsuir.activity_tracker_mobile.ui.onboarding.OnboardingViewModelFactory
import com.bsuir.activity_tracker_mobile.ui.screen.ActivityScreen
import com.bsuir.activity_tracker_mobile.ui.screen.DashboardScreen
import com.bsuir.activity_tracker_mobile.ui.screen.FinanceScreen
import com.bsuir.activity_tracker_mobile.ui.screen.LanguageScreen
import com.bsuir.activity_tracker_mobile.ui.screen.NutritionScreen
import com.bsuir.activity_tracker_mobile.ui.screen.ProfileScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ActivityTrackerApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val context = LocalContext.current
    val services = remember { ServiceLocator.provide(context) }

    val showBottomBar = currentRoute in setOf(
        Screen.Dashboard.route,
        Screen.Activity.route,
        Screen.Nutrition.route,
        Screen.Finance.route,
        Screen.Profile.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.AuthGate,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.AuthGate) {
                AuthGateScreen(
                    authRepository = services.authRepository,
                    profileRepository = services.profileRepository,
                    onNavigateToLogin = {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.AuthGate) { inclusive = true }
                        }
                    },
                    onNavigateToOnboarding = {
                        navController.navigate(Routes.OnboardingProfile) {
                            popUpTo(Routes.AuthGate) { inclusive = true }
                        }
                    },
                    onNavigateToMain = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Routes.AuthGate) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.Login) {
                val vm: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(
                        services.authRepository,
                        services.validateAuthInputUseCase,
                        services.appContext
                    )
                )
                LoginScreen(
                    viewModel = vm,
                    onGoToRegister = { navController.navigate(Routes.Register) },
                    onSuccess = {
                        navController.navigate(Routes.AuthGate) {
                            popUpTo(Routes.Login) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.Register) {
                val vm: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(
                        services.authRepository,
                        services.validateAuthInputUseCase,
                        services.appContext
                    )
                )
                RegisterScreen(
                    viewModel = vm,
                    onGoToLogin = { navController.popBackStack() },
                    onSuccess = {
                        navController.navigate(Routes.AuthGate) {
                            popUpTo(Routes.Register) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.OnboardingProfile) {
                val vm: OnboardingViewModel = viewModel(
                    factory = OnboardingViewModelFactory(
                        services.profileRepository,
                        services.validateProfileInputUseCase,
                        services.appContext
                    )
                )
                OnboardingProfileScreen(
                    viewModel = vm,
                    onCompleted = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Routes.OnboardingProfile) { inclusive = true }
                        }
                    }
                )
            }

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
                ProfileScreen(
                    onOpenLanguage = { navController.navigate(Routes.Language) }
                )
            }
            composable(Routes.Language) {
                LanguageScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
