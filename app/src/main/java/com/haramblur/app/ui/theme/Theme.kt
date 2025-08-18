package com.haramblur.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light theme color scheme based on our design plan
private val LightColorScheme = lightColorScheme(
    primary = IslamicGreen,
    onPrimary = PureWhite,
    secondary = SoftGray,
    onSecondary = DeepBlack,
    tertiary = InfoBlue,
    background = PureWhite,
    onBackground = DeepBlack,
    surface = PureWhite,
    onSurface = DeepBlack,
    error = ErrorRed,
    onError = PureWhite
)

// Dark theme color scheme based on our design plan
private val DarkColorScheme = darkColorScheme(
    primary = IslamicGreen,
    onPrimary = DarkSurface,
    secondary = DarkSurface,
    onSecondary = OnDarkSurface,
    tertiary = InfoBlue,
    background = DarkBackground,
    onBackground = OnDarkBackground,
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    error = DarkCodeRed,
    onError = OnDarkBackground
)

@Composable
fun HaramBlurTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, but we'll disable it by default
    // to maintain our Islamic-themed color scheme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    // Update system bars to match our theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            
            // Set up edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            // Update the system bars
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
            
            // Make system bars transparent and let the content draw behind them
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}