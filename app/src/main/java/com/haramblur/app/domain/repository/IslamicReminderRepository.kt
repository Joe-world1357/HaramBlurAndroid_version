package com.haramblur.app.domain.repository

import com.haramblur.app.domain.model.IslamicReminder
import com.haramblur.app.domain.model.ReminderCategory
import com.haramblur.app.domain.model.ReminderType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Islamic reminder operations
 */
interface IslamicReminderRepository {
    
    /**
     * Insert a new Islamic reminder
     */
    suspend fun insertReminder(reminder: IslamicReminder)
    
    /**
     * Update an existing reminder
     */
    suspend fun updateReminder(reminder: IslamicReminder)
    
    /**
     * Delete a reminder
     */
    suspend fun deleteReminder(id: Int)
    
    /**
     * Get all reminders
     */
    fun getAllReminders(): Flow<List<IslamicReminder>>
    
    /**
     * Get reminders by type
     */
    fun getRemindersByType(type: ReminderType): Flow<List<IslamicReminder>>
    
    /**
     * Get reminders by category
     */
    fun getRemindersByCategory(category: ReminderCategory): Flow<List<IslamicReminder>>
    
    /**
     * Get favorite reminders
     */
    fun getFavoriteReminders(): Flow<List<IslamicReminder>>
    
    /**
     * Get a random reminder based on category weights
     */
    suspend fun getRandomReminder(categories: List<ReminderCategory> = emptyList()): IslamicReminder?
    
    /**
     * Get context-appropriate reminder
     */
    suspend fun getContextualReminder(
        category: ReminderCategory,
        excludeRecent: Boolean = true,
        recentHours: Int = 24
    ): IslamicReminder?
    
    /**
     * Mark reminder as shown
     */
    suspend fun markReminderShown(id: Int)
    
    /**
     * Toggle favorite status
     */
    suspend fun toggleFavorite(id: Int)
    
    /**
     * Search reminders by text
     */
    suspend fun searchReminders(query: String): List<IslamicReminder>
    
    /**
     * Initialize default reminders
     */
    suspend fun initializeDefaultReminders()
    
    /**
     * Get reminder statistics
     */
    suspend fun getReminderStats(): ReminderStats
}

/**
 * Statistics for Islamic reminders
 */
data class ReminderStats(
    val totalReminders: Int = 0,
    val quranVerses: Int = 0,
    val hadithCount: Int = 0,
    val duasCount: Int = 0,
    val favoriteCount: Int = 0,
    val mostShownCategory: ReminderCategory? = null,
    val totalUsage: Int = 0,
    val lastShownTime: Long? = null
)