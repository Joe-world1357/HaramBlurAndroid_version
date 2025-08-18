package com.haramblur.app.data.repository

import com.haramblur.app.data.local.dao.DetectionDao
import com.haramblur.app.data.local.entity.toDomain
import com.haramblur.app.data.local.entity.toEntity
import com.haramblur.app.domain.model.DetectionResult
import com.haramblur.app.domain.model.DetectionType
import com.haramblur.app.domain.repository.DetectionRepository
import com.haramblur.app.domain.repository.DetectionStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of DetectionRepository
 */
@Singleton
class DetectionRepositoryImpl @Inject constructor(
    private val detectionDao: DetectionDao
) : DetectionRepository {
    
    override suspend fun insertDetection(detection: DetectionResult) {
        detectionDao.insertDetection(detection.toEntity())
    }
    
    override fun getAllDetections(): Flow<List<DetectionResult>> {
        return detectionDao.getAllDetections().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getDetectionsByType(type: DetectionType): Flow<List<DetectionResult>> {
        return detectionDao.getDetectionsByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getTodayDetections(): Flow<List<DetectionResult>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        
        return detectionDao.getTodayDetections(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getDetectionsInRange(startTime: Long, endTime: Long): Flow<List<DetectionResult>> {
        return detectionDao.getDetectionsInRange(startTime, endTime).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getDetectionStats(): DetectionStats {
        val totalDetections = detectionDao.getTotalDetectionCount()
        
        // Calculate today's range
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        
        val todayDetections = detectionDao.getTodayDetectionCount(startOfDay, endOfDay)
        
        // Calculate week range
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startOfWeek = calendar.timeInMillis
        val weekDetections = detectionDao.getWeekDetectionCount(startOfWeek)
        
        // Calculate month range
        calendar.add(Calendar.DAY_OF_YEAR, -23) // Total 30 days
        val startOfMonth = calendar.timeInMillis
        val monthDetections = detectionDao.getMonthDetectionCount(startOfMonth)
        
        val mostCommonTypeResult = detectionDao.getMostCommonDetectionType()
        val mostCommonType = mostCommonTypeResult?.let { 
            try {
                DetectionType.valueOf(it.detectionType)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        
        val averageProcessingTime = detectionDao.getAverageProcessingTime() ?: 0f
        val lastDetectionTime = detectionDao.getLastDetectionTime()
        
        return DetectionStats(
            totalDetections = totalDetections,
            todayDetections = todayDetections,
            weekDetections = weekDetections,
            monthDetections = monthDetections,
            mostCommonType = mostCommonType,
            averageProcessingTime = averageProcessingTime,
            lastDetectionTime = lastDetectionTime
        )
    }
    
    override suspend fun deleteOldDetections(retentionDays: Int) {
        val cutoffTime = System.currentTimeMillis() - (retentionDays * 24 * 60 * 60 * 1000L)
        detectionDao.deleteOldDetections(cutoffTime)
    }
    
    override suspend fun clearAllDetections() {
        detectionDao.clearAllDetections()
    }
}