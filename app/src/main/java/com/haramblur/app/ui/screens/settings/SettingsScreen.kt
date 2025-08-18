package com.haramblur.app.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.haramblur.app.ui.components.HaramBlurTopAppBar
import com.haramblur.app.ui.theme.HaramBlurTheme
import com.haramblur.app.ui.theme.Spacing

/**
 * Settings screen of the application
 *
 * @param navigateBack Function to navigate back
 * @param contentPadding Padding values for the screen content
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit = {},
    windowSizeClass: com.haramblur.app.ui.util.WindowSizeClass? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // Determine layout based on screen size
    val isCompactWidth = windowSizeClass?.widthSizeClass == com.haramblur.app.ui.util.WindowWidthSizeClass.Compact
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Top App Bar
        HaramBlurTopAppBar(
            title = "Settings",
            showBackButton = true,
            onBackClick = navigateBack
        )

        // Content with responsive layout
        if (isCompactWidth) {
            // Phone layout - vertical scrolling list
            SettingsPhoneLayout()
        } else {
            // Tablet layout - two-column settings
            SettingsTabletLayout()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    HaramBlurTheme {
        SettingsScreen()
    }
}
