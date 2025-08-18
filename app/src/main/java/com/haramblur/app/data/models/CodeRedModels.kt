package com.haramblur.app.data.models

/**
 * Represents a CODE RED lock session
 */
data class LockSession(
    val id: Long = 0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = 0,  // 0 means ongoing
    val lockedApps: List<String> = emptyList(),
    val reason: String = "",
    val isActive: Boolean = true
)

/**
 * Represents a predefined lock preset
 */
data class LockPreset(
    val id: Long = 0,
    val name: String,
    val apps: List<String>,
    val durationMinutes: Int
)

/**
 * Represents an app that can be locked
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val isSystemApp: Boolean = false,
    val isLocked: Boolean = false
)
