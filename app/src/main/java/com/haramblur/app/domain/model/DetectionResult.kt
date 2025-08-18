package com.haramblur.app.domain.model

import java.util.UUID

/**
 * Domain model representing the result of content detection
 */
data class DetectionResult(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val detectionType: DetectionType,
    val confidenceScore: Float,
    val actionTaken: ActionType,
    val appPackage: String? = null,
    val contentHash: String? = null,
    val processingTimeMs: Int = 0,
    val boundingBoxes: List<BoundingBox> = emptyList()
)

/**
 * Types of content detection
 */
enum class DetectionType {
    FEMALE_FACE,
    NSFW_CONTENT,
    NUDITY,
    INAPPROPRIATE_WEBSITE,
    TEXT_CONTENT
}

/**
 * Actions taken when content is detected
 */
enum class ActionType {
    BLUR_APPLIED,
    BLACK_BOX_OVERLAY,
    WEBSITE_BLOCKED,
    NOTIFICATION_SHOWN,
    NO_ACTION,
    ISLAMIC_REMINDER_SHOWN
}

/**
 * Bounding box for detected content regions
 */
data class BoundingBox(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val confidence: Float
)