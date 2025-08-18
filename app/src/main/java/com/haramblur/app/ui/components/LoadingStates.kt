package com.haramblur.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.haramblur.app.ui.theme.AlertRed
import com.haramblur.app.ui.theme.IslamicGreen
import com.haramblur.app.ui.theme.Spacing
import com.haramblur.app.ui.theme.WarningAmber

/**
 * Loading state for UI components
 */
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
    data class NetworkError(val message: String) : UiState()
}

/**
 * Loading indicator with spinner
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large)
    ) {
        CircularProgressIndicator(
            color = IslamicGreen,
            modifier = Modifier.size(48.dp)
        )
    }
}

/**
 * Pulsating loading indicator
 */
@Composable
fun PulsatingLoadingIndicator(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = IslamicGreen,
            modifier = Modifier
                .size(48.dp * scale)
        )
    }
}

/**
 * Error state display
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large)
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = AlertRed,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AlertRed
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.large)
        )
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

/**
 * Network error state display
 */
@Composable
fun NetworkErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large)
    ) {
        // Rotating wifi icon
        val infiniteTransition = rememberInfiniteTransition(label = "rotate")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rotate"
        )
        
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = null,
            tint = WarningAmber,
            modifier = Modifier
                .size(64.dp)
                .rotate(rotation)
        )
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        Text(
            text = "Network Error",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = WarningAmber
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.large)
        )
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

/**
 * UI state handler that shows appropriate content based on state
 */
@Composable
fun UiStateHandler(
    state: UiState,
    onRetry: () -> Unit = {},
    content: @Composable () -> Unit
) {
    when (state) {
        is UiState.Loading -> {
            LoadingIndicator()
        }
        is UiState.Success -> {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                content()
            }
        }
        is UiState.Error -> {
            ErrorState(
                message = state.message,
                onRetry = onRetry
            )
        }
        is UiState.NetworkError -> {
            NetworkErrorState(
                message = state.message,
                onRetry = onRetry
            )
        }
    }
}
