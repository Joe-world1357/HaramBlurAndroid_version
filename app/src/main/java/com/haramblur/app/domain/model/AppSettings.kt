package com.haramblur.app.domain.model

/**
 * Domain model representing app settings and preferences
 */
data class AppSettings(
    val protectionEnabled: Boolean = true,
    val detectionSensitivity: Float = 0.8f,
    val processingFrameRate: Int = 3,
    val blurType: BlurType = BlurType.GAUSSIAN,
    val blurIntensity: BlurIntensity = BlurIntensity.MEDIUM,
    val enableWebsiteBlocking: Boolean = true,
    val enableIslamicReminders: Boolean = true,
    val reminderFrequency: ReminderFrequency = ReminderFrequency.NORMAL,
    val preferredLanguage: String = "en",
    val enableArabicText: Boolean = true,
    val dataRetentionDays: Int = 30,
    val performanceMode: PerformanceMode = PerformanceMode.BALANCED,
    val enableNotifications: Boolean = true,
    val enableVibration: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

/**
 * Types of blur effects
 */
enum class BlurType {
    GAUSSIAN,
    BLACK_BOX,
    PIXELATION,
    ISLAMIC_PATTERN
}

/**
 * Blur intensity levels
 */
enum class BlurIntensity {
    LIGHT,
    MEDIUM,
    STRONG
}

/**
 * Frequency of Islamic reminders
 */
enum class ReminderFrequency {
    LOW,
    NORMAL,
    HIGH,
    VERY_HIGH
}

/**
 * Performance modes
 */
enum class PerformanceMode {
    HIGH_PERFORMANCE,
    BALANCED,
    BATTERY_SAVER
}

/**
 * Theme modes
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}