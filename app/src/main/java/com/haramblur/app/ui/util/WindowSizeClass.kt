package com.haramblur.app.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Window size classes for responsive UI design
 */
enum class WindowWidthSizeClass { Compact, Medium, Expanded }
enum class WindowHeightSizeClass { Compact, Medium, Expanded }

/**
 * Window size class for width and height
 */
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
)

/**
 * Calculate the window size class based on the current configuration
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    
    return remember(configuration) {
        WindowSizeClass(
            widthSizeClass = screenWidth.toWindowWidthSizeClass(),
            heightSizeClass = screenHeight.toWindowHeightSizeClass()
        )
    }
}

/**
 * Convert width in dp to WindowWidthSizeClass
 */
fun Dp.toWindowWidthSizeClass(): WindowWidthSizeClass = when {
    this < 600.dp -> WindowWidthSizeClass.Compact    // Phone
    this < 840.dp -> WindowWidthSizeClass.Medium     // Small tablet / foldable
    else -> WindowWidthSizeClass.Expanded             // Large tablet
}

/**
 * Convert height in dp to WindowHeightSizeClass
 */
fun Dp.toWindowHeightSizeClass(): WindowHeightSizeClass = when {
    this < 480.dp -> WindowHeightSizeClass.Compact    // Small phone
    this < 900.dp -> WindowHeightSizeClass.Medium     // Phone / small tablet
    else -> WindowHeightSizeClass.Expanded             // Large tablet
}
