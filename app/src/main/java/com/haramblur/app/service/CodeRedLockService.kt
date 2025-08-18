package com.haramblur.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haramblur.app.domain.repository.CodeRedRepository
import com.haramblur.app.domain.repository.IslamicReminderRepository
import com.haramblur.app.domain.model.ReminderCategory
import com.haramblur.app.ui.theme.HaramBlurTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Accessibility service for CODE RED app locking functionality
 */
@AndroidEntryPoint
class CodeRedLockService : AccessibilityService() {
    
    @Inject
    lateinit var codeRedRepository: CodeRedRepository
    
    @Inject
    lateinit var islamicReminderRepository: IslamicReminderRepository
    
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
        }
        
        serviceInfo = info
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Start monitoring active sessions
        monitorActiveSessions()
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let { handleAccessibilityEvent(it) }
    }
    
    override fun onInterrupt() {
        // Handle service interruption
    }
    
    private fun handleAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null) {
                serviceScope.launch {
                    checkIfAppShouldBeLocked(packageName)
                }
            }
        }
    }
    
    private suspend fun checkIfAppShouldBeLocked(packageName: String) {
        try {
            val activeSession = codeRedRepository.getActiveSession()
            if (activeSession != null && packageName in activeSession.lockedApps) {
                showLockOverlay(packageName)
                
                // Force app to background
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    private suspend fun showLockOverlay(packageName: String) {
        if (overlayView != null) return
        
        val reminder = islamicReminderRepository.getContextualReminder(
            ReminderCategory.LOWERING_GAZE
        )
        
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }
        
        overlayView = ComposeView(this).apply {
            setContent {
                HaramBlurTheme {
                    LockOverlayContent(
                        packageName = packageName,
                        reminder = reminder,
                        onDismiss = { hideLockOverlay() }
                    )
                }
            }
        }
        
        try {
            windowManager?.addView(overlayView, layoutParams)
            
            // Auto-dismiss after 5 seconds
            serviceScope.launch {
                delay(5000)
                hideLockOverlay()
            }
        } catch (e: Exception) {
            // Handle overlay creation error
            overlayView = null
        }
    }
    
    private fun hideLockOverlay() {
        overlayView?.let { view ->
            try {
                windowManager?.removeView(view)
            } catch (e: Exception) {
                // Handle removal error
            }
        }
        overlayView = null
    }
    
    private fun monitorActiveSessions() {
        serviceScope.launch {
            codeRedRepository.getActiveSessionFlow().collect { session ->
                if (session == null) {
                    hideLockOverlay()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        hideLockOverlay()
        serviceScope.cancel()
    }
}

@Composable
private fun LockOverlayContent(
    packageName: String,
    reminder: com.haramblur.app.domain.model.IslamicReminder?,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "App Locked",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "This app is currently locked by CODE RED",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (reminder != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = reminder.englishTranslation,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "- ${reminder.sourceReference}",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}