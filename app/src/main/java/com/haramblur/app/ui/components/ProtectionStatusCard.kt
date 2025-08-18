package com.haramblur.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.ErrorRed
import com.haramblur.app.ui.theme.Spacing
import com.haramblur.app.ui.theme.SuccessGreen
import com.haramblur.app.ui.theme.WarningAmber

/**
 * State of the protection system
 */
enum class ProtectionState {
    ACTIVE,
    INACTIVE,
    SETUP_NEEDED
}

/**
 * Card showing the current protection status of the app
 *
 * @param state Current protection state
 * @param activeTime How long protection has been active (if active)
 * @param detectionCount Number of detections today
 * @param onToggleProtection Callback when protection toggle is switched
 * @param onSetupClick Callback when setup button is clicked (if setup is needed)
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun ProtectionStatusCard(
    state: ProtectionState,
    activeTime: String = "3h 24m",
    detectionCount: Int = 2,
    onToggleProtection: (Boolean) -> Unit = {},
    onSetupClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (icon, title, description, color) = when (state) {
        ProtectionState.ACTIVE -> Quadruple(
            Icons.Outlined.Shield,
            "Protection is ACTIVE",
            "Real-time monitoring enabled",
            SuccessGreen
        )
        ProtectionState.INACTIVE -> Quadruple(
            Icons.Default.Warning,
            "Protection is OFF",
            "Tap to enable protection",
            ErrorRed
        )
        ProtectionState.SETUP_NEEDED -> Quadruple(
            Icons.Default.Warning,
            "Setup Required",
            "Missing permissions",
            WarningAmber
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .padding(Spacing.medium)
                .fillMaxWidth()
        ) {
            // Header with icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(Spacing.small))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Toggle switch for active/inactive states
                if (state != ProtectionState.SETUP_NEEDED) {
                    Switch(
                        checked = state == ProtectionState.ACTIVE,
                        onCheckedChange = onToggleProtection
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            // Protection state indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Protection level dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val dotCount = 5
                    val activeDotIndex = when (state) {
                        ProtectionState.ACTIVE -> 0
                        ProtectionState.INACTIVE -> 1
                        ProtectionState.SETUP_NEEDED -> 2
                    }
                    
                    repeat(dotCount) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (index == activeDotIndex) color else Color.Gray.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            // Setup button for SETUP_NEEDED state
            if (state == ProtectionState.SETUP_NEEDED) {
                androidx.compose.material3.Button(
                    onClick = onSetupClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("COMPLETE SETUP")
                }
            } else {
                // Stats for ACTIVE or INACTIVE states
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Detections today
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = "$detectionCount detections today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Active time for ACTIVE state
                    if (state == ProtectionState.ACTIVE) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            Text(
                                text = "Active for $activeTime",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        // Risk warning for INACTIVE state
                        Text(
                            text = "Your privacy is at risk",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper class for grouping 4 values
 */
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
