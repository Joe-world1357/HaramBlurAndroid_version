package com.haramblur.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.AlertRed
import com.haramblur.app.ui.theme.DarkRed
import com.haramblur.app.ui.theme.LightRed
import com.haramblur.app.ui.theme.PureWhite
import com.haramblur.app.ui.theme.Spacing

/**
 * CODE RED alert card for emergency app locking
 *
 * @param onActivateClick Callback for when the CODE RED button is clicked
 * @param modifier Modifier for customizing the layout
 */
@Composable
fun CodeRedAlertCard(
    onActivateClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Detect dark theme by checking if background color is dark
    val isDarkTheme = MaterialTheme.colorScheme.background.red < 0.5f
    
    // Use different colors based on theme
    val cardBackgroundColor = if (isDarkTheme) {
        DarkRed.copy(alpha = 0.2f)
    } else {
        LightRed
    }
    
    val titleColor = if (isDarkTheme) {
        Color(0xFFFF8A80) // Lighter red for dark theme
    } else {
        AlertRed
    }
    
    val buttonColor = if (isDarkTheme) {
        Color(0xFFFF5252) // Brighter red for dark theme
    } else {
        AlertRed
    }
    
    val textColor = if (isDarkTheme) {
        Color.White.copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Spacing.medium)
                .fillMaxWidth()
        ) {
            // Title with emoji
            Text(
                text = "🚨 Emergency App Lock",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Description
            Text(
                text = "Lock distracting apps instantly",
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(Spacing.large))
            
            // CODE RED button with improved visibility
            Button(
                onClick = onActivateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = PureWhite
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "ACTIVATE CODE RED",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
