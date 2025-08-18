package com.haramblur.app.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.data.models.IslamicReminder
import com.haramblur.app.data.models.ReminderCategory

/**
 * Room entity for storing Islamic reminders
 */
@Entity(tableName = "islamic_reminders")
data class IslamicReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quote: String,
    val source: String,
    val category: String  // Stored as string from enum
)

/**
 * Extension functions to convert between domain model and entity
 */
fun IslamicReminderEntity.toDomainModel(): IslamicReminder {
    return IslamicReminder(
        id = id,
        quote = quote,
        source = source,
        category = ReminderCategory.valueOf(category)
    )
}

fun IslamicReminder.toEntity(): IslamicReminderEntity {
    return IslamicReminderEntity(
        id = id,
        quote = quote,
        source = source,
        category = category.name
    )
}
