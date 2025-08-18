package com.haramblur.app.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.haramblur.app.ui.navigation.NavDestination as AppDestination
import com.haramblur.app.ui.theme.AlertRed

/**
 * Bottom navigation bar component for the app
 *
 * @param navController Navigation controller for handling navigation between screens
 * @param currentDestination Current destination from NavBackStackEntry
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun HaramBlurBottomNavigation(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        // Display navigation items for main destinations
        AppDestination.mainDestinations.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any { 
                it.route == destination.route 
            } ?: false
            
            // Special styling for CODE RED item
            val isCodeRed = destination == AppDestination.CodeRed
            
            BottomNavigationItem(
                destination = destination,
                isSelected = isSelected,
                isCodeRed = isCodeRed,
                onClick = {
                    // Navigate only if we're not already at this destination
                    try {
                        navController.navigate(destination.route) {
                            // Pop up to the start destination of the graph to avoid building up
                            // a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        // Handle navigation error gracefully
                        e.printStackTrace()
                    }
                }
            )
        }
    }
}

/**
 * Individual navigation item in the bottom navigation bar
 */
@Composable
private fun RowScope.BottomNavigationItem(
    destination: AppDestination,
    isSelected: Boolean,
    isCodeRed: Boolean = false,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            if (isCodeRed) {
                // Special styling for CODE RED with badge
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = AlertRed
                        )
                    }
                ) {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title,
                        tint = if (isSelected) AlertRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                // Regular icon
                Icon(
                    imageVector = destination.icon,
                    contentDescription = destination.title
                )
            }
        },
        label = {
            Text(
                text = destination.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isCodeRed && isSelected) AlertRed else Color.Unspecified
            )
        },
        colors = if (isCodeRed) {
            // Custom colors for CODE RED
            NavigationBarItemDefaults.colors(
                selectedIconColor = AlertRed,
                selectedTextColor = AlertRed,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        } else {
            NavigationBarItemDefaults.colors()
        }
    )
}
