package com.haramblur.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.domain.model.AuthenticityGrade
import com.haramblur.app.domain.model.IslamicReminder
import com.haramblur.app.domain.model.ReminderCategory
import com.haramblur.app.domain.model.ReminderType

/**
 * Room entity for Islamic reminders
 */
@Entity(tableName = "islamic_reminders")
data class IslamicReminderEntity(
    @PrimaryKey
    val id: Int,
    val type: String,
    val category: String,
    val arabicText: String,
    val englishTranslation: String,
    val transliteration: String?,
    val sourceReference: String,
    val narrator: String?,
    val authenticityGrade: String?,
    val themeTagsJson: String, // JSON string of tags
    val usageCount: Int,
    val lastShown: Long?,
    val isActive: Boolean,
    val isFavorite: Boolean,
    val createdDate: Long,
    val modifiedDate: Long
)

/**
 * Convert domain model to entity
 */
fun IslamicReminder.toEntity(): IslamicReminderEntity {
    return IslamicReminderEntity(
        id = id,
        type = type.name,
        category = category.name,
        arabicText = arabicText,
        englishTranslation = englishTranslation,
        transliteration = transliteration,
        sourceReference = sourceReference,
        narrator = narrator,
        authenticityGrade = authenticityGrade?.name,
        themeTagsJson = themeTags.joinToString(","),
        usageCount = usageCount,
        lastShown = lastShown,
        isActive = isActive,
        isFavorite = isFavorite,
        createdDate = createdDate,
        modifiedDate = modifiedDate
    )
}

/**
 * Convert entity to domain model
 */
fun IslamicReminderEntity.toDomain(): IslamicReminder {
    return IslamicReminder(
        id = id,
        type = ReminderType.valueOf(type),
        category = ReminderCategory.valueOf(category),
        arabicText = arabicText,
        englishTranslation = englishTranslation,
        transliteration = transliteration,
        sourceReference = sourceReference,
        narrator = narrator,
        authenticityGrade = authenticityGrade?.let { AuthenticityGrade.valueOf(it) },
        themeTags = if (themeTagsJson.isBlank()) emptyList() else themeTagsJson.split(","),
        usageCount = usageCount,
        lastShown = lastShown,
        isActive = isActive,
        isFavorite = isFavorite,
        createdDate = createdDate,
        modifiedDate = modifiedDate
    )
}