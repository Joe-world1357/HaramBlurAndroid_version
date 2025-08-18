package com.haramblur.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing all navigation destinations in the app
 */
sealed class NavDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    // Main screen destinations accessible from bottom navigation
    object Home : NavDestination("home", "Home", Icons.Default.Home)
    object Stats : NavDestination("stats", "Stats", Icons.Outlined.BarChart)
    object Settings : NavDestination("settings", "Settings", Icons.Default.Settings)
    object CodeRed : NavDestination("code_red", "CODE RED", Icons.Default.Warning)
    
    // Other screens not directly accessible from bottom navigation
    object DetectionSettings : NavDestination("detection_settings", "Detection", Icons.Default.Settings)
    object BlockingPreferences : NavDestination("blocking_preferences", "Blocking", Icons.Default.Settings)
    object IslamicReminders : NavDestination("islamic_reminders", "Reminders", Icons.Default.Settings)
    object PrivacySettings : NavDestination("privacy_settings", "Privacy", Icons.Default.Settings)
    object AppPreferences : NavDestination("app_preferences", "App", Icons.Default.Settings)
    object HelpSupport : NavDestination("help_support", "Help", Icons.Default.Settings)

    companion object {
        // List of main destinations shown in the bottom navigation
        val mainDestinations = listOf(Home, Stats, Settings, CodeRed)
    }
}
