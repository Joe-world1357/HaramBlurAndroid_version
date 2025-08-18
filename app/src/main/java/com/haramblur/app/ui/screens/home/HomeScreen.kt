package com.haramblur.app.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.haramblur.app.ui.util.WindowSizeClass
import com.haramblur.app.ui.util.WindowWidthSizeClass
import com.haramblur.app.ui.components.CodeRedAlertCard
import com.haramblur.app.ui.components.HaramBlurTopAppBar
import com.haramblur.app.ui.components.IslamicReminderCard
import com.haramblur.app.ui.components.ProtectionState
import com.haramblur.app.ui.components.ProtectionStatusCard
import com.haramblur.app.ui.components.TodayProtectionCard
import com.haramblur.app.ui.theme.HaramBlurTheme
import com.haramblur.app.ui.theme.Spacing
import com.haramblur.app.presentation.viewmodel.HomeViewModel

/**
 * Home screen of the application
 *
 * @param navigateToStats Function to navigate to the statistics screen
 * @param navigateToCodeRed Function to navigate to the CODE RED screen
 * @param navigateToProfile Function to navigate to the profile screen
 * @param navigateToNotifications Function to navigate to the notifications screen
 * @param contentPadding Padding values for the screen content
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun HomeScreen(
    navigateToStats: () -> Unit = {},
    navigateToCodeRed: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToNotifications: () -> Unit = {},
    windowSizeClass: WindowSizeClass? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Collect state from ViewModel
    val settings by viewModel.settings.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    // Convert settings to protection state
    val protectionState = if (settings.protectionEnabled) {
        ProtectionState.ACTIVE
    } else {
        ProtectionState.INACTIVE
    }
    
    // Determine layout based on screen size
    val isCompactWidth = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Compact
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Top App Bar
        HaramBlurTopAppBar(
            onProfileClick = navigateToProfile,
            onNotificationClick = navigateToNotifications
        )
        
        // Content with responsive layout
        if (isCompactWidth) {
            // Phone layout - vertical scrolling column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.medium)
            ) {
                // Protection Status Card
                ProtectionStatusCard(
                    state = protectionState,
                    onToggleProtection = { isActive ->
                        viewModel.toggleProtection()
                    }
                )
                
                // CODE RED Alert Card
                CodeRedAlertCard(
                    onActivateClick = navigateToCodeRed
                )
                
                // Today's Protection Stats Card
                TodayProtectionCard(
                    onViewDetailsClick = navigateToStats
                )
                
                // Islamic Reminder Card
                IslamicReminderCard()
            }
        } else {
            // Tablet layout - grid with multiple columns
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.medium)
            ) {
                // Left column - main cards
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .verticalScroll(rememberScrollState())
                        .padding(end = Spacing.medium)
                ) {
                    // Protection Status Card
                    ProtectionStatusCard(
                        state = protectionState.value,
                        onToggleProtection = { isActive ->
                            protectionState.value = if (isActive) ProtectionState.ACTIVE else ProtectionState.INACTIVE
                        }
                    )
                    
                    // CODE RED Alert Card
                    CodeRedAlertCard(
                        onActivateClick = navigateToCodeRed
                    )
                }
                
                // Right column - secondary cards
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .verticalScroll(rememberScrollState())
                        .padding(start = Spacing.medium)
                ) {
                    // Today's Protection Stats Card
                    TodayProtectionCard(
                        onViewDetailsClick = navigateToStats
                    )
                    
                    // Islamic Reminder Card
                    IslamicReminderCard()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HaramBlurTheme {
        HomeScreen()
    }
}
