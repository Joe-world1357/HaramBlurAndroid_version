package com.haramblur.app.domain.repository

import com.haramblur.app.domain.model.DetectionResult
import com.haramblur.app.domain.model.DetectionType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for detection-related operations
 */
interface DetectionRepository {
    
    /**
     * Insert a new detection result
     */
    suspend fun insertDetection(detection: DetectionResult)
    
    /**
     * Get all detection results as a flow
     */
    fun getAllDetections(): Flow<List<DetectionResult>>
    
    /**
     * Get detections by type
     */
    fun getDetectionsByType(type: DetectionType): Flow<List<DetectionResult>>
    
    /**
     * Get detections for today
     */
    fun getTodayDetections(): Flow<List<DetectionResult>>
    
    /**
     * Get detections for a specific date range
     */
    fun getDetectionsInRange(startTime: Long, endTime: Long): Flow<List<DetectionResult>>
    
    /**
     * Get detection statistics
     */
    suspend fun getDetectionStats(): DetectionStats
    
    /**
     * Delete old detections based on retention policy
     */
    suspend fun deleteOldDetections(retentionDays: Int)
    
    /**
     * Clear all detection data
     */
    suspend fun clearAllDetections()
}

/**
 * Statistics for detections
 */
data class DetectionStats(
    val totalDetections: Int = 0,
    val todayDetections: Int = 0,
    val weekDetections: Int = 0,
    val monthDetections: Int = 0,
    val mostCommonType: DetectionType? = null,
    val averageProcessingTime: Float = 0f,
    val lastDetectionTime: Long? = null
)