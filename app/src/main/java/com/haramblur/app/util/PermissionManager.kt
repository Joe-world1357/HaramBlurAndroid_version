package com.haramblur.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

/**
 * Manages permissions required for HaramBlur functionality
 */
object PermissionManager {
    
    /**
     * Check if overlay permission is granted
     */
    fun hasOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }
    
    /**
     * Request overlay permission
     */
    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            if (context is Activity) {
                context.startActivity(intent)
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
    
    /**
     * Check if accessibility service is enabled
     */
    fun hasAccessibilityPermission(context: Context): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val serviceName = "${context.packageName}/.service.CodeRedLockService"
        return enabledServices?.contains(serviceName) == true
    }
    
    /**
     * Request accessibility permission
     */
    fun requestAccessibilityPermission(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        if (context is Activity) {
            context.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
    
    /**
     * Request screen capture permission
     */
    fun requestScreenCapturePermission(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val mediaProjectionManager = activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = mediaProjectionManager.createScreenCaptureIntent()
        launcher.launch(intent)
    }
    
    /**
     * Check if all required permissions are granted
     */
    fun hasAllRequiredPermissions(context: Context): Boolean {
        return hasOverlayPermission(context) && hasAccessibilityPermission(context)
    }
    
    /**
     * Get list of missing permissions
     */
    fun getMissingPermissions(context: Context): List<String> {
        val missing = mutableListOf<String>()
        
        if (!hasOverlayPermission(context)) {
            missing.add("System Alert Window")
        }
        
        if (!hasAccessibilityPermission(context)) {
            missing.add("Accessibility Service")
        }
        
        return missing
    }
}