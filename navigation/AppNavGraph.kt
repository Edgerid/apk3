package com.personalfitnessos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.personalfitnessos.core.di.ViewModelFactory
import com.personalfitnessos.ui.home.HomeScreen
import com.personalfitnessos.ui.nutrition.NutritionScreen
import com.personalfitnessos.ui.onboarding.OnboardingScreen
import com.personalfitnessos.ui.onboarding.OnboardingViewModel
import com.personalfitnessos.ui.progress.ProgressScreen
import com.personalfitnessos.ui.settings.MoreScreen
import com.personalfitnessos.ui.workout.WorkoutScreen

private const val ROUTE_ONBOARDING = "onboarding"
private const val ROUTE_MAIN_SHELL = "main_shell"

/**
 * Root shell: a Scaffold with the bottom nav bar (spec §4) and a NavHost for the five
 * top-level destinations, gated behind onboarding (spec §5) until a profile exists.
 * [startAtOnboarding] is resolved once by the caller (see MainActivity / RootViewModel)
 * before this graph is ever composed, so there is no flash of the wrong screen.
 */
@Composable
fun AppNavGraph(viewModelFactory: ViewModelFactory, startAtOnboarding: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (startAtOnboarding) ROUTE_ONBOARDING else ROUTE_MAIN_SHELL,
    ) {
        composable(ROUTE_ONBOARDING) {
            val onboardingViewModel: OnboardingViewModel = viewModel(factory = viewModelFactory)
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onFinished = {
                    navController.navigate(ROUTE_MAIN_SHELL) {
                        popUpTo(ROUTE_ONBOARDING) { inclusive = true }
                    }
                },
            )
        }
        composable(ROUTE_MAIN_SHELL) {
            MainShell()
        }
    }
}

@Composable
private fun MainShell() {
    val shellNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by shellNavController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            shellNavController.navigate(destination.route) {
                                popUpTo(shellNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = shellNavController,
            startDestination = TopLevelDestination.HOME.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(TopLevelDestination.HOME.route) { HomeScreen() }
            composable(TopLevelDestination.NUTRITION.route) { NutritionScreen() }
            composable(TopLevelDestination.WORKOUT.route) { WorkoutScreen() }
            composable(TopLevelDestination.PROGRESS.route) { ProgressScreen() }
            composable(TopLevelDestination.MORE.route) { MoreScreen() }
        }
    }
}

