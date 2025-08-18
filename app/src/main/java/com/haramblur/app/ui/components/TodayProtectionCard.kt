package com.haramblur.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.Spacing

/**
 * Card showing today's protection statistics
 *
 * @param contentBlockedCount Number of content items blocked today
 * @param websitesBlockedCount Number of websites blocked today
 * @param appsLockedCount Number of apps currently locked
 * @param onViewDetailsClick Callback for when the "View Details" button is clicked
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun TodayProtectionCard(
    contentBlockedCount: Int = 5,
    websitesBlockedCount: Int = 3,
    appsLockedCount: Int = 2,
    onViewDetailsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
            // Card title
            Text(
                text = "Today's Protection",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Content blocked stat
            StatRow(
                icon = Icons.Outlined.BarChart,
                title = "$contentBlockedCount Content Blocked",
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Websites blocked stat
            StatRow(
                icon = Icons.Outlined.Block,
                title = "$websitesBlockedCount Websites Blocked",
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Apps locked stat
            StatRow(
                icon = Icons.Outlined.Phone,
                title = "$appsLockedCount Apps Currently Locked",
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // View details button
            Button(
                onClick = onViewDetailsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("View Details >")
            }
        }
    }
}

/**
 * Row displaying a statistic with an icon and text
 */
@Composable
private fun StatRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.padding(end = Spacing.small)
        )
        
        Spacer(modifier = Modifier.width(Spacing.small))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
