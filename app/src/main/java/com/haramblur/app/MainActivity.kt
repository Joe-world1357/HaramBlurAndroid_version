package com.haramblur.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hierarchy
import com.haramblur.app.ui.components.HaramBlurBottomNavigation
import androidx.compose.runtime.remember
import com.haramblur.app.ui.components.HaramBlurNavigationRail
import com.haramblur.app.ui.navigation.NavDestination
import com.haramblur.app.ui.screens.home.HomeScreen
import com.haramblur.app.ui.screens.stats.StatsScreen
import com.haramblur.app.ui.screens.settings.SettingsScreen
import com.haramblur.app.ui.screens.codered.CodeRedScreen
import com.haramblur.app.ui.theme.HaramBlurTheme
import com.haramblur.app.ui.util.WindowSizeClass
import com.haramblur.app.ui.util.WindowWidthSizeClass
import com.haramblur.app.ui.util.animatedComposable
import com.haramblur.app.ui.util.navigateWithAnimation
import com.haramblur.app.ui.util.rememberWindowSizeClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HaramBlurTheme {
                HaramBlurApp()
            }
        }
    }
}

/**
 * Main application composable that sets up the navigation and UI structure
 */
@Composable
fun HaramBlurApp() {
    val navController = rememberNavController()
    val windowSizeClass = rememberWindowSizeClass()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Use different navigation patterns based on screen size
    val useBottomNavigation = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (useBottomNavigation) {
                HaramBlurBottomNavigation(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize()) {
            // For tablet/wider screens, show navigation rail on the side
            if (!useBottomNavigation) {
                HaramBlurNavigationRail(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
            
            // Main content area
            NavHost(
                navController = navController,
                startDestination = NavDestination.Home.route,
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(1f)
            ) {
                animatedComposable(NavDestination.Home.route) {
                    HomeScreen(
                        navigateToStats = { navController.navigateWithAnimation(NavDestination.Stats.route) },
                        navigateToCodeRed = { navController.navigateWithAnimation(NavDestination.CodeRed.route) },
                        windowSizeClass = windowSizeClass
                    )
                }
                
                animatedComposable(NavDestination.Stats.route) {
                    StatsScreen(
                        navigateBack = { navController.popBackStack() },
                        windowSizeClass = windowSizeClass
                    )
                }
                
                animatedComposable(NavDestination.Settings.route) {
                    SettingsScreen(
                        navigateBack = { navController.popBackStack() },
                        windowSizeClass = windowSizeClass
                    )
                }
                
                animatedComposable(NavDestination.CodeRed.route) {
                    CodeRedScreen(
                        navigateBack = { navController.popBackStack() },
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HaramBlurAppPreview() {
    HaramBlurTheme {
        HaramBlurApp()
    }
}