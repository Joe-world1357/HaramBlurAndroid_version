package com.haramblur.app.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.components.HaramBlurTopAppBar
import com.haramblur.app.ui.theme.HaramBlurTheme
import com.haramblur.app.ui.theme.Spacing
import com.haramblur.app.ui.util.WindowSizeClass
import com.haramblur.app.ui.util.WindowWidthSizeClass

/**
 * Statistics screen of the application
 *
 * @param navigateBack Function to navigate back
 * @param contentPadding Padding values for the screen content
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun StatsScreen(
    navigateBack: () -> Unit = {},
    windowSizeClass: WindowSizeClass? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // Determine layout based on screen size
    val isCompactWidth = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Compact
    
    // State for selected tab
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }
    val tabTitles = listOf("Today", "Week", "Month")
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Top App Bar
        HaramBlurTopAppBar(
            title = "Statistics",
            showBackButton = true,
            onBackClick = navigateBack
        )
        
        // Tab row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { setSelectedTab(index) },
                    text = { Text(title) }
                )
            }
        }

        // Content with responsive layout
        if (isCompactWidth) {
            // Phone layout - vertical scrolling column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.medium)
            ) {
                // Protection Summary Card
                ProtectionSummaryCard()
                
                // Weekly Chart Card
                WeeklyChartCard()
                
                // Top Blocked Sites Card
                TopBlockedSitesCard()
            }
        } else {
            // Tablet layout - grid with multiple columns
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.medium)
            ) {
                // Protection Summary in a row
                ProtectionSummaryCard()
                
                // Charts and lists
                WeeklyChartCard(
                    modifier = Modifier.padding(horizontal = Spacing.medium)
                )
                
                TopBlockedSitesCard(
                    modifier = Modifier.padding(horizontal = Spacing.medium)
                )
            }
        }
    }
}

@Composable
fun ProtectionSummaryCard() {
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
                text = "Protection Summary",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Stats row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Content Blocked
                StatItem(
                    icon = Icons.Outlined.Shield,
                    value = "5",
                    label = "Content\nBlocked"
                )
                
                // Sites Blocked
                StatItem(
                    icon = Icons.Outlined.Block,
                    value = "3",
                    label = "Sites\nBlocked"
                )
                
                // Apps Locked
                StatItem(
                    icon = Icons.Outlined.PhoneAndroid,
                    value = "2",
                    label = "Apps\nLocked"
                )
                
                // Active Time
                StatItem(
                    icon = Icons.Outlined.Timer,
                    value = "7h",
                    label = "Active\nTime"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeeklyChartCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Weekly Chart",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            Text(
                text = "Content Detections",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // Simple bar chart
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Sample data for the week
                val values = listOf(4, 8, 6, 3, 6, 4, 10)
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                val maxValue = values.maxOrNull() ?: 1
                
                values.forEachIndexed { index, value ->
                    val heightPercent = value.toFloat() / maxValue.toFloat()
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Value label
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Bar
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height((heightPercent * 100).dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Day label
                        Text(
                            text = days[index],
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopBlockedSitesCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Top Blocked Sites",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // List of top blocked sites
            BlockedSiteItem(
                rank = 1,
                site = "instagram.com",
                count = 12
            )
            
            BlockedSiteItem(
                rank = 2,
                site = "tiktok.com",
                count = 8
            )
            
            BlockedSiteItem(
                rank = 3,
                site = "youtube.com",
                count = 6
            )
            
            BlockedSiteItem(
                rank = 4,
                site = "facebook.com",
                count = 4
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            // View all button
            TextButton(
                onClick = {},
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("View All >")
            }
        }
    }
}

@Composable
fun BlockedSiteItem(
    rank: Int,
    site: String,
    count: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rank.",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(24.dp)
            )
            
            Text(
                text = site,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Text(
            text = "($count times)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    HaramBlurTheme {
        StatsScreen()
    }
}
