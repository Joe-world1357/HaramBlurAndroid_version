package com.haramblur.app.domain.model

/**
 * Domain model representing Islamic reminders (Quran verses, Hadith, Duas)
 */
data class IslamicReminder(
    val id: Int,
    val type: ReminderType,
    val category: ReminderCategory,
    val arabicText: String,
    val englishTranslation: String,
    val transliteration: String? = null,
    val sourceReference: String,
    val narrator: String? = null,
    val authenticityGrade: AuthenticityGrade? = null,
    val themeTags: List<String> = emptyList(),
    val usageCount: Int = 0,
    val lastShown: Long? = null,
    val isActive: Boolean = true,
    val isFavorite: Boolean = false,
    val createdDate: Long = System.currentTimeMillis(),
    val modifiedDate: Long = System.currentTimeMillis()
)

/**
 * Types of Islamic reminders
 */
enum class ReminderType {
    QURAN,
    HADITH,
    DUA,
    CUSTOM
}

/**
 * Categories of Islamic reminders
 */
enum class ReminderCategory {
    LOWERING_GAZE,
    PURITY,
    TAQWA,
    GENERAL,
    FAMILY,
    PRAYER,
    DHIKR,
    SEEKING_FORGIVENESS
}

/**
 * Authenticity grades for Hadith
 */
enum class AuthenticityGrade {
    SAHIH,
    HASAN,
    DAEEF
}