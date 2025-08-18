package com.haramblur.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haramblur.app.ui.screens.home.HomeScreen
import com.haramblur.app.ui.screens.codered.CodeRedScreen
import com.haramblur.app.ui.screens.settings.SettingsScreen
import com.haramblur.app.ui.screens.stats.StatsScreen

/**
 * Main navigation host for the application
 *
 * @param navController Navigation controller for handling navigation between screens
 * @param startDestination Starting route for the navigation
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavDestination.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen
        composable(NavDestination.Home.route) {
            HomeScreen(
                navigateToStats = { navController.navigate(NavDestination.Stats.route) },
                navigateToCodeRed = { navController.navigate(NavDestination.CodeRed.route) },
                navigateToProfile = { /* Navigate to profile when implemented */ },
                navigateToNotifications = { /* Navigate to notifications when implemented */ }
            )
        }
        
        // Stats screen
        composable(NavDestination.Stats.route) {
            StatsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        
        // Settings screen
        composable(NavDestination.Settings.route) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        
        // CODE RED screen
        composable(NavDestination.CodeRed.route) {
            CodeRedScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
