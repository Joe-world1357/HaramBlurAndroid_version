package com.haramblur.app.data.models

/**
 * Represents the current protection status of the application
 */
enum class ProtectionStatus {
    ACTIVE,       // Protection is active and monitoring
    INACTIVE,     // Protection is turned off
    SETUP_NEEDED  // Protection needs setup or configuration
}

/**
 * Represents the protection statistics for a given time period
 */
data class ProtectionStats(
    val contentBlocked: Int = 0,
    val websitesBlocked: Int = 0,
    val appsLocked: Int = 0,
    val activeTimeMinutes: Int = 0
)

/**
 * Represents a detection event
 */
data class DetectionEvent(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val contentType: ContentType,
    val source: String,  // App name or website URL
    val action: DetectionAction
)

/**
 * Types of content that can be detected
 */
enum class ContentType {
    FEMALE_FACE,
    NSFW_CONTENT,
    NUDITY,
    SUGGESTIVE_CONTENT,
    OTHER
}

/**
 * Actions taken when content is detected
 */
enum class DetectionAction {
    BLURRED,
    BLOCKED,
    WARNED,
    IGNORED
}
