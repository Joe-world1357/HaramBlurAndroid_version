package com.haramblur.app.data.repositories

import com.haramblur.app.data.dao.DetectionEventDao
import com.haramblur.app.data.models.ContentType
import com.haramblur.app.data.models.DetectionAction
import com.haramblur.app.data.models.DetectionEvent
import com.haramblur.app.data.models.ProtectionStats
import com.haramblur.app.data.models.entities.toDomainModel
import com.haramblur.app.data.models.entities.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Repository for detection-related operations
 */
class DetectionRepository(private val detectionEventDao: DetectionEventDao) {

    /**
     * Record a new detection event
     */
    suspend fun recordDetection(
        contentType: ContentType,
        source: String,
        action: DetectionAction
    ): Long {
        val event = DetectionEvent(
            timestamp = System.currentTimeMillis(),
            contentType = contentType,
            source = source,
            action = action
        )
        return detectionEventDao.insert(event.toEntity())
    }

    /**
     * Get all detection events
     */
    fun getAllEvents(): Flow<List<DetectionEvent>> {
        return detectionEventDao.getAllEvents().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Get detection events for today
     */
    fun getTodayEvents(): Flow<List<DetectionEvent>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        return detectionEventDao.getEventsBetween(startOfDay, System.currentTimeMillis())
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    /**
     * Get detection events for the past week
     */
    fun getWeekEvents(): Flow<List<DetectionEvent>> {
        val weekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        return detectionEventDao.getEventsBetween(weekAgo, System.currentTimeMillis())
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    /**
     * Get detection events for the past month
     */
    fun getMonthEvents(): Flow<List<DetectionEvent>> {
        val monthAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        return detectionEventDao.getEventsBetween(monthAgo, System.currentTimeMillis())
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    /**
     * Get protection statistics for today
     */
    suspend fun getTodayStats(): ProtectionStats {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        val events = detectionEventDao.getEventsBetween(startOfDay, System.currentTimeMillis()).first()
        
        val contentBlocked = events.count { 
            it.action == DetectionAction.BLURRED.name || it.action == DetectionAction.BLOCKED.name 
        }
        
        // Count unique websites blocked
        val websitesBlocked = events
            .filter { it.action == DetectionAction.BLOCKED.name && it.source.contains("http") }
            .map { it.source.split("/")[2] } // Extract domain
            .distinct()
            .size
        
        // Count unique apps with blocked content
        val appsBlocked = events
            .filter { it.action == DetectionAction.BLOCKED.name && !it.source.contains("http") }
            .map { it.source }
            .distinct()
            .size
        
        // Placeholder for active time - in a real app this would be tracked separately
        val activeTimeMinutes = 60
        
        return ProtectionStats(
            contentBlocked = contentBlocked,
            websitesBlocked = websitesBlocked,
            appsLocked = appsBlocked,
            activeTimeMinutes = activeTimeMinutes
        )
    }

    /**
     * Clear all detection history
     */
    suspend fun clearHistory() {
        detectionEventDao.deleteAll()
    }
}
