package com.haramblur.app.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.haramblur.app.data.models.LockSession

/**
 * Room entity for storing lock sessions
 */
@Entity(tableName = "lock_sessions")
data class LockSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val lockedApps: String,  // Stored as comma-separated list
    val reason: String,
    val isActive: Boolean
)

/**
 * Type converters for Room
 */
class Converters {
    @TypeConverter
    fun fromAppList(apps: List<String>): String {
        return apps.joinToString(",")
    }

    @TypeConverter
    fun toAppList(appsString: String): List<String> {
        return if (appsString.isEmpty()) {
            emptyList()
        } else {
            appsString.split(",")
        }
    }
}

/**
 * Extension functions to convert between domain model and entity
 */
fun LockSessionEntity.toDomainModel(): LockSession {
    return LockSession(
        id = id,
        startTime = startTime,
        endTime = endTime,
        lockedApps = lockedApps.split(",").filter { it.isNotEmpty() },
        reason = reason,
        isActive = isActive
    )
}

fun LockSession.toEntity(): LockSessionEntity {
    return LockSessionEntity(
        id = id,
        startTime = startTime,
        endTime = endTime,
        lockedApps = lockedApps.joinToString(","),
        reason = reason,
        isActive = isActive
    )
}
