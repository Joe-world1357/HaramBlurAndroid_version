package com.haramblur.app.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.data.models.LockPreset

/**
 * Room entity for storing lock presets
 */
@Entity(tableName = "lock_presets")
data class LockPresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val apps: String,  // Stored as comma-separated list
    val durationMinutes: Int
)

/**
 * Extension functions to convert between domain model and entity
 */
fun LockPresetEntity.toDomainModel(): LockPreset {
    return LockPreset(
        id = id,
        name = name,
        apps = apps.split(",").filter { it.isNotEmpty() },
        durationMinutes = durationMinutes
    )
}

fun LockPreset.toEntity(): LockPresetEntity {
    return LockPresetEntity(
        id = id,
        name = name,
        apps = apps.joinToString(","),
        durationMinutes = durationMinutes
    )
}
