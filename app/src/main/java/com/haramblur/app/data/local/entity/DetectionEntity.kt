package com.haramblur.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.domain.model.ActionType
import com.haramblur.app.domain.model.BoundingBox
import com.haramblur.app.domain.model.DetectionResult
import com.haramblur.app.domain.model.DetectionType

/**
 * Room entity for detection results
 */
@Entity(tableName = "detection_logs")
data class DetectionEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val detectionType: String,
    val confidenceScore: Float,
    val actionTaken: String,
    val appPackage: String?,
    val contentHash: String?,
    val processingTimeMs: Int,
    val boundingBoxesJson: String? // JSON string of bounding boxes
)

/**
 * Convert domain model to entity
 */
fun DetectionResult.toEntity(): DetectionEntity {
    return DetectionEntity(
        id = id,
        timestamp = timestamp,
        detectionType = detectionType.name,
        confidenceScore = confidenceScore,
        actionTaken = actionTaken.name,
        appPackage = appPackage,
        contentHash = contentHash,
        processingTimeMs = processingTimeMs,
        boundingBoxesJson = if (boundingBoxes.isNotEmpty()) {
            // Convert to JSON string - simplified for now
            boundingBoxes.joinToString(";") { "${it.x},${it.y},${it.width},${it.height},${it.confidence}" }
        } else null
    )
}

/**
 * Convert entity to domain model
 */
fun DetectionEntity.toDomain(): DetectionResult {
    return DetectionResult(
        id = id,
        timestamp = timestamp,
        detectionType = DetectionType.valueOf(detectionType),
        confidenceScore = confidenceScore,
        actionTaken = ActionType.valueOf(actionTaken),
        appPackage = appPackage,
        contentHash = contentHash,
        processingTimeMs = processingTimeMs,
        boundingBoxes = boundingBoxesJson?.let { json ->
            // Parse JSON string - simplified for now
            json.split(";").mapNotNull { box ->
                val parts = box.split(",")
                if (parts.size == 5) {
                    BoundingBox(
                        x = parts[0].toFloat(),
                        y = parts[1].toFloat(),
                        width = parts[2].toFloat(),
                        height = parts[3].toFloat(),
                        confidence = parts[4].toFloat()
                    )
                } else null
            }
        } ?: emptyList()
    )
}