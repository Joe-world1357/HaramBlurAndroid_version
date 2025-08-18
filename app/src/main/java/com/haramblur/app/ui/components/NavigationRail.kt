package com.haramblur.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.haramblur.app.ui.navigation.NavDestination as AppDestination
import com.haramblur.app.ui.theme.AlertRed
import com.haramblur.app.ui.theme.Spacing

/**
 * Navigation rail for tablet layouts
 */
@Composable
fun HaramBlurNavigationRail(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = Spacing.small),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // App logo or icon could go here
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Display navigation items for main destinations
        AppDestination.mainDestinations.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any { 
                it.route == destination.route 
            } ?: false
            
            // Special styling for CODE RED item
            val isCodeRed = destination == AppDestination.CodeRed
            
            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    try {
                        navController.navigate(destination.route) {
                            popUpTo(0)
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
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
                    NavigationRailItemDefaults.colors(
                        selectedIconColor = AlertRed,
                        selectedTextColor = AlertRed,
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                } else {
                    NavigationRailItemDefaults.colors()
                }
            )
        }
    }
}
