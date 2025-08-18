package com.haramblur.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.Spacing

@Composable
fun SettingsPhoneLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.medium)
    ) {
        // Settings categories
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
        
        Divider(modifier = Modifier.padding(vertical = Spacing.medium))
        
        SettingsCategoryHeader(title = "Blocking Preferences")
        SettingsItem(
            title = "Website Categories",
            description = "Select categories of websites to block",
            icon = Icons.Outlined.Category,
            onClick = {}
        )
        SettingsItem(
            title = "Custom Block Lists",
            description = "Manage your custom website blocklist",
            icon = Icons.Outlined.PlaylistAdd,
            onClick = {}
        )
        SettingsItem(
            title = "Blocking Actions",
            description = "What happens when content is blocked",
            icon = Icons.Outlined.Block,
            onClick = {}
        )
        
        Divider(modifier = Modifier.padding(vertical = Spacing.medium))
        
        SettingsCategoryHeader(title = "Islamic Reminders")
        SettingsItem(
            title = "Reminder Types",
            description = "Choose which reminders to show",
            icon = Icons.Outlined.Notifications,
            onClick = {}
        )
        SettingsItem(
            title = "Display Duration",
            description = "How long reminders should show",
            icon = Icons.Outlined.Timer,
            onClick = {}
        )
        SettingsItem(
            title = "Language Settings",
            description = "Choose language for reminders",
            icon = Icons.Outlined.Language,
            onClick = {}
        )
        
        Divider(modifier = Modifier.padding(vertical = Spacing.medium))
        
        SettingsCategoryHeader(title = "Privacy & Security")
        SettingsItem(
            title = "Data Retention",
            description = "Control how long data is kept",
            icon = Icons.Outlined.DataUsage,
            onClick = {}
        )
        SettingsItem(
            title = "Usage Logs",
            description = "Manage app usage history",
            icon = Icons.Outlined.History,
            onClick = {}
        )
        SettingsItem(
            title = "Export Settings",
            description = "Export your configuration",
            icon = Icons.Outlined.SaveAlt,
            onClick = {}
        )
        
        Divider(modifier = Modifier.padding(vertical = Spacing.medium))
        
        SettingsCategoryHeader(title = "App Preferences")
        SettingsItem(
            title = "Theme Settings",
            description = "Customize app appearance",
            icon = Icons.Outlined.Palette,
            onClick = {}
        )
        SettingsItem(
            title = "Notifications",
            description = "Configure app notifications",
            icon = Icons.Outlined.NotificationsActive,
            onClick = {}
        )
        SettingsItem(
            title = "Performance",
            description = "Adjust app performance settings",
            icon = Icons.Outlined.Speed,
            onClick = {}
        )
        
        Divider(modifier = Modifier.padding(vertical = Spacing.medium))
        
        SettingsCategoryHeader(title = "Help & Support")
        SettingsItem(
            title = "Setup Guide",
            description = "Learn how to set up HaramBlur",
            icon = Icons.Outlined.Help,
            onClick = {}
        )
        SettingsItem(
            title = "Troubleshooting",
            description = "Fix common issues",
            icon = Icons.Outlined.BugReport,
            onClick = {}
        )
        SettingsItem(
            title = "Contact Support",
            description = "Get help from our team",
            icon = Icons.Outlined.Email,
            onClick = {}
        )
        
        // Add some space at the bottom
        Spacer(modifier = Modifier.height(Spacing.large))
    }
}

@Composable
fun SettingsCategoryHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    )
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.small)
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = Spacing.small)
            )
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Chevron icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
