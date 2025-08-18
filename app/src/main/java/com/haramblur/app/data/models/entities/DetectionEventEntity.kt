package com.haramblur.app.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.data.models.ContentType
import com.haramblur.app.data.models.DetectionAction
import com.haramblur.app.data.models.DetectionEvent

/**
 * Room entity for storing detection events
 */
@Entity(tableName = "detection_events")
data class DetectionEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val contentType: String,  // Stored as string from enum
    val source: String,
    val action: String  // Stored as string from enum
)

/**
 * Extension functions to convert between domain model and entity
 */
fun DetectionEventEntity.toDomainModel(): DetectionEvent {
    return DetectionEvent(
        id = id,
        timestamp = timestamp,
        contentType = ContentType.valueOf(contentType),
        source = source,
        action = DetectionAction.valueOf(action)
    )
}

fun DetectionEvent.toEntity(): DetectionEventEntity {
    return DetectionEventEntity(
        id = id,
        timestamp = timestamp,
        contentType = contentType.name,
        source = source,
        action = action.name
    )
}
