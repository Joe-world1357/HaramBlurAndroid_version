package com.haramblur.app.data.models

/**
 * Represents an Islamic reminder quote
 */
data class IslamicReminder(
    val id: Long = 0,
    val quote: String,
    val source: String,
    val category: ReminderCategory = ReminderCategory.GENERAL
)

/**
 * Categories of Islamic reminders
 */
enum class ReminderCategory {
    GENERAL,
    LOWERING_GAZE,
    PURITY,
    SELF_CONTROL,
    PRAYER,
    PATIENCE
}
