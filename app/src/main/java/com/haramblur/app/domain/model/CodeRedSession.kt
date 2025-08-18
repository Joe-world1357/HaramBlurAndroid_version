package com.haramblur.app.domain.model

import java.util.UUID

/**
 * Domain model representing a CODE RED app locking session
 */
data class CodeRedSession(
    val sessionId: String = UUID.randomUUID().toString(),
    val startTime: Long,
    val endTime: Long,
    val lockedApps: List<String>, // Package names
    val passwordHash: String,
    val reason: String? = null,
    val status: SessionStatus = SessionStatus.ACTIVE,
    val unlockAttempts: Int = 0,
    val lastUnlockAttempt: Long? = null
)

/**
 * Status of a CODE RED session
 */
enum class SessionStatus {
    ACTIVE,
    COMPLETED,
    TERMINATED,
    EXPIRED
}

/**
 * Represents an app that can be locked
 */
data class LockableApp(
    val packageName: String,
    val appName: String,
    val icon: ByteArray? = null,
    val category: AppCategory = AppCategory.OTHER,
    val isSystemApp: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as LockableApp
        
        if (packageName != other.packageName) return false
        if (appName != other.appName) return false
        if (category != other.category) return false
        if (isSystemApp != other.isSystemApp) return false
        if (icon != null) {
            if (other.icon == null) return false
            if (!icon.contentEquals(other.icon)) return false
        } else if (other.icon != null) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + appName.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + isSystemApp.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * Categories of apps that can be locked
 */
enum class AppCategory {
    SOCIAL_MEDIA,
    ENTERTAINMENT,
    DATING,
    GAMING,
    BROWSER,
    MESSAGING,
    OTHER
}