package com.haramblur.app.data.local.dao

import androidx.room.*
import com.haramblur.app.data.local.entity.DetectionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for detection results
 */
@Dao
interface DetectionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetection(detection: DetectionEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetections(detections: List<DetectionEntity>)
    
    @Update
    suspend fun updateDetection(detection: DetectionEntity)
    
    @Delete
    suspend fun deleteDetection(detection: DetectionEntity)
    
    @Query("SELECT * FROM detection_logs ORDER BY timestamp DESC")
    fun getAllDetections(): Flow<List<DetectionEntity>>
    
    @Query("SELECT * FROM detection_logs WHERE detectionType = :type ORDER BY timestamp DESC")
    fun getDetectionsByType(type: String): Flow<List<DetectionEntity>>
    
    @Query("""
        SELECT * FROM detection_logs 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        ORDER BY timestamp DESC
    """)
    fun getTodayDetections(startOfDay: Long, endOfDay: Long): Flow<List<DetectionEntity>>
    
    @Query("""
        SELECT * FROM detection_logs 
        WHERE timestamp >= :startTime AND timestamp <= :endTime 
        ORDER BY timestamp DESC
    """)
    fun getDetectionsInRange(startTime: Long, endTime: Long): Flow<List<DetectionEntity>>
    
    @Query("SELECT COUNT(*) FROM detection_logs")
    suspend fun getTotalDetectionCount(): Int
    
    @Query("SELECT COUNT(*) FROM detection_logs WHERE timestamp >= :startOfDay AND timestamp < :endOfDay")
    suspend fun getTodayDetectionCount(startOfDay: Long, endOfDay: Long): Int
    
    @Query("SELECT COUNT(*) FROM detection_logs WHERE timestamp >= :startOfWeek")
    suspend fun getWeekDetectionCount(startOfWeek: Long): Int
    
    @Query("SELECT COUNT(*) FROM detection_logs WHERE timestamp >= :startOfMonth")
    suspend fun getMonthDetectionCount(startOfMonth: Long): Int
    
    @Query("SELECT detectionType, COUNT(*) as count FROM detection_logs GROUP BY detectionType ORDER BY count DESC LIMIT 1")
    suspend fun getMostCommonDetectionType(): DetectionTypeCount?
    
    @Query("SELECT AVG(processingTimeMs) FROM detection_logs WHERE processingTimeMs > 0")
    suspend fun getAverageProcessingTime(): Float?
    
    @Query("SELECT MAX(timestamp) FROM detection_logs")
    suspend fun getLastDetectionTime(): Long?
    
    @Query("DELETE FROM detection_logs WHERE timestamp < :cutoffTime")
    suspend fun deleteOldDetections(cutoffTime: Long): Int
    
    @Query("DELETE FROM detection_logs")
    suspend fun clearAllDetections()
    
    @Query("SELECT * FROM detection_logs WHERE id = :id")
    suspend fun getDetectionById(id: String): DetectionEntity?
}

/**
 * Data class for detection type count query result
 */
data class DetectionTypeCount(
    val detectionType: String,
    val count: Int
)