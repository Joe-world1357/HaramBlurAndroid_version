package com.haramblur.app.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.Spacing

@Composable
fun SettingsTabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.medium)
    ) {
        // Left column - Categories
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(end = Spacing.medium)
        ) {
            SettingsCategoryCard(
                title = "Detection Settings",
                icon = Icons.Outlined.FilterAlt,
                isSelected = true,
                onClick = {}
            )
            SettingsCategoryCard(
                title = "Blocking Preferences",
                icon = Icons.Outlined.Block,
                isSelected = false,
                onClick = {}
            )
            SettingsCategoryCard(
                title = "Islamic Reminders",
                icon = Icons.Outlined.Notifications,
                isSelected = false,
                onClick = {}
            )
            SettingsCategoryCard(
                title = "Privacy & Security",
                icon = Icons.Outlined.Security,
                isSelected = false,
                onClick = {}
            )
            SettingsCategoryCard(
                title = "App Preferences",
                icon = Icons.Outlined.Settings,
                isSelected = false,
                onClick = {}
            )
            SettingsCategoryCard(
                title = "Help & Support",
                icon = Icons.Outlined.Help,
                isSelected = false,
                onClick = {}
            )
        }
        
        // Vertical divider
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        )
        
        // Right column - Settings for selected category
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(start = Spacing.medium)
        ) {
            // Show settings for "Detection Settings" category
            SettingsCategoryHeader(title = "Detection Settings")
            
            SettingsItem(
                title = "Content Types",
                description = "Choose what content to detect and blur",
                icon = Icons.Outlined.FilterAlt,
                onClick = {}
            )
            SettingsItem(
                title = "Sensitivity Levels",
                description = "Adjust detection sensitivity",
                icon = Icons.Outlined.Tune,
                onClick = {}
            )
            SettingsItem(
                title = "Blur Effects",
                description = "Customize how content is blurred",
                icon = Icons.Outlined.BlurOn,
                onClick = {}
            )
            
            // Content types section
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        text = "Content Types to Detect",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.medium))
                    
                    // Content type options
                    CheckboxSettingItem(
                        title = "Female faces and figures",
                        isChecked = true,
                        onCheckedChange = {}
                    )
                    
                    CheckboxSettingItem(
                        title = "NSFW/Adult content",
                        isChecked = true,
                        onCheckedChange = {}
                    )
                    
                    CheckboxSettingItem(
                        title = "Nudity detection",
                        isChecked = true,
                        onCheckedChange = {}
                    )
                    
                    CheckboxSettingItem(
                        title = "Suggestive content",
                        isChecked = false,
                        onCheckedChange = {}
                    )
                }
            }
            
            // Sensitivity section
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        text = "Detection Sensitivity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.medium))
                    
                    // Sensitivity slider
                    Text(
                        text = "Currently: Medium",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.small))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Low",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Slider(
                            value = 0.5f,
                            onValueChange = {},
                            modifier = Modifier.weight(1f)
                        )
                        
                        Text(
                            text = "High",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCategoryCard(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium,
        border = if (isSelected) 
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary) 
        else 
            null,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.width(Spacing.medium))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CheckboxSettingItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
