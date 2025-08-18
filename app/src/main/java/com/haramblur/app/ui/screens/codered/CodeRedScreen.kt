package com.haramblur.app.ui.screens.codered

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.components.HaramBlurTopAppBar
import com.haramblur.app.ui.theme.AlertRed
import com.haramblur.app.ui.theme.HaramBlurTheme
import com.haramblur.app.ui.theme.PureWhite
import com.haramblur.app.ui.theme.Spacing
import com.haramblur.app.ui.util.WindowSizeClass
import com.haramblur.app.ui.util.WindowWidthSizeClass
import androidx.compose.foundation.layout.FlowRow

/**
 * CODE RED emergency screen of the application
 *
 * @param navigateBack Function to navigate back
 * @param contentPadding Padding values for the screen content
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun CodeRedScreen(
    navigateBack: () -> Unit = {},
    windowSizeClass: WindowSizeClass? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // Determine layout based on screen size
    val isCompactWidth = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Compact
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Top App Bar
        HaramBlurTopAppBar(
            title = "🚨 CODE RED EMERGENCY",
            showBackButton = true,
            onBackClick = navigateBack
        )

        // Content with responsive layout
        if (isCompactWidth) {
            // Phone layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.medium)
            ) {
                // Quick Lock Presets
                QuickLockPresetsCard()
                
                // Custom Lock
                CustomLockCard()
                
                // Active Sessions
                ActiveSessionsCard()
            }
        } else {
            // Tablet layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.medium)
            ) {
                // Left column - Main controls
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .verticalScroll(rememberScrollState())
                        .padding(end = Spacing.medium)
                ) {
                    CustomLockCard()
                }
                
                // Right column - Quick presets and active sessions
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .verticalScroll(rememberScrollState())
                        .padding(start = Spacing.medium)
                ) {
                    QuickLockPresetsCard()
                    ActiveSessionsCard()
                }
            }
        }
    }
}

@Composable
fun QuickLockPresetsCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Quick Lock Presets",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Quick lock preset items
            QuickLockPresetItem(
                title = "Social Media",
                appCount = 5,
                duration = "15 min",
                onClick = {}
            )
            
            QuickLockPresetItem(
                title = "Gaming",
                appCount = 3,
                duration = "1 hour",
                onClick = {}
            )
            
            QuickLockPresetItem(
                title = "Entertainment",
                appCount = 4,
                duration = "30 min",
                onClick = {}
            )
            
            QuickLockPresetItem(
                title = "Messaging",
                appCount = 6,
                duration = "2 hours",
                onClick = {}
            )
        }
    }
}

@Composable
fun QuickLockPresetItem(
    title: String,
    appCount: Int,
    duration: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.PhoneAndroid,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(Spacing.small))
            
            Text(
                text = "$title ($appCount apps)",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Text(
            text = duration,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CustomLockCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Custom Lock",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Text(
                text = "Select Apps to Lock:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // App selection grid - using simple Row/Column layout instead of FlowRow
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "Instagram", isChecked = true, onCheckedChange = {})
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "TikTok", isChecked = true, onCheckedChange = {})
                    }
                }
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "YouTube", isChecked = true, onCheckedChange = {})
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "Twitter", isChecked = false, onCheckedChange = {})
                    }
                }
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "Facebook", isChecked = false, onCheckedChange = {})
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        AppCheckItem(name = "Snapchat", isChecked = true, onCheckedChange = {})
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Duration selection
            Text(
                text = "Duration:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Duration dropdown (simplified for now)
            OutlinedTextField(
                value = "1 Hour",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Password field
            Text(
                text = "Password:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            OutlinedTextField(
                value = "",
                onValueChange = {},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Reason field
            Text(
                text = "Reason:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            OutlinedTextField(
                value = "Fighting urges...",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Spacing.large))
            
            // Activate button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlertRed,
                    contentColor = PureWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "🚨 ACTIVATE LOCK",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AppCheckItem(
    name: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = Spacing.small, vertical = Spacing.xs)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ActiveSessionsCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Active Sessions",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhoneAndroid,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(Spacing.small))
                
                Text(
                    text = "2 apps locked",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(Spacing.small))
                
                Text(
                    text = "1h 34m remaining",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = {}) {
                    Text("View Details")
                }
                
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AlertRed
                    )
                ) {
                    Text("Emergency Unlock")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CodeRedScreenPreview() {
    HaramBlurTheme {
        CodeRedScreen()
    }
}
