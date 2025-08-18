package com.haramblur.app.domain.model

/**
 * Domain model representing a blocked website
 */
data class BlockedSite(
    val domain: String,
    val category: SiteCategory,
    val firstBlocked: Long,
    val blockCount: Int = 1,
    val lastBlocked: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val userAdded: Boolean = false,
    val reason: String? = null
)

/**
 * Categories of blocked sites
 */
enum class SiteCategory {
    ADULT_CONTENT,
    SOCIAL_MEDIA,
    DATING,
    GAMBLING,
    ENTERTAINMENT,
    CUSTOM,
    UNKNOWN
}