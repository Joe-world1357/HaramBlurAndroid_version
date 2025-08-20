package com.haramblur.app.data.local.dao

import androidx.room.*
import com.haramblur.app.data.local.entity.IslamicReminderEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Islamic reminders
 */
@Dao
interface IslamicReminderDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: IslamicReminderEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<IslamicReminderEntity>)
    
    @Update
    suspend fun updateReminder(reminder: IslamicReminderEntity)
    
    @Delete
    suspend fun deleteReminder(reminder: IslamicReminderEntity)
    
    @Query("DELETE FROM islamic_reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
    
    @Query("SELECT * FROM islamic_reminders WHERE isActive = 1 ORDER BY id ASC")
    fun getAllActiveReminders(): Flow<List<IslamicReminderEntity>>
    
    @Query("SELECT * FROM islamic_reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<IslamicReminderEntity>>
    
    @Query("SELECT * FROM islamic_reminders WHERE type = :type AND isActive = 1 ORDER BY id ASC")
    fun getRemindersByType(type: String): Flow<List<IslamicReminderEntity>>
    
    @Query("SELECT * FROM islamic_reminders WHERE category = :category AND isActive = 1 ORDER BY id ASC")
    fun getRemindersByCategory(category: String): Flow<List<IslamicReminderEntity>>
    
    @Query("SELECT * FROM islamic_reminders WHERE isFavorite = 1 AND isActive = 1 ORDER BY id ASC")
    fun getFavoriteReminders(): Flow<List<IslamicReminderEntity>>
    
    @Query("SELECT * FROM islamic_reminders WHERE id = :id")
    suspend fun getReminderById(id: Int): IslamicReminderEntity?
    
    @Query("""
        SELECT * FROM islamic_reminders 
        WHERE isActive = 1 
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun getRandomReminder(): IslamicReminderEntity?
    
    @Query("""
        SELECT * FROM islamic_reminders 
        WHERE category = :category AND isActive = 1 
        AND (lastShown IS NULL OR lastShown < :excludeAfterTime)
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun getRandomReminderByCategory(category: String, excludeAfterTime: Long): IslamicReminderEntity?
    
    @Query("""
        SELECT * FROM islamic_reminders 
        WHERE category IN (:categories) AND isActive = 1 
        AND (lastShown IS NULL OR lastShown < :excludeAfterTime)
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun getRandomReminderByCategories(categories: List<String>, excludeAfterTime: Long): IslamicReminderEntity?
    
    @Query("UPDATE islamic_reminders SET usageCount = usageCount + 1, lastShown = :timestamp WHERE id = :id")
    suspend fun markReminderShown(id: Int, timestamp: Long)
    
    @Query("UPDATE islamic_reminders SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)
    
    @Query("""
        SELECT * FROM islamic_reminders 
        WHERE isActive = 1 AND (
            arabicText LIKE '%' || :query || '%' OR 
            englishTranslation LIKE '%' || :query || '%' OR 
            sourceReference LIKE '%' || :query || '%' OR
            themeTagsJson LIKE '%' || :query || '%'
        )
        ORDER BY id ASC
    """)
    suspend fun searchReminders(query: String): List<IslamicReminderEntity>
    
    @Query("SELECT COUNT(*) FROM islamic_reminders WHERE isActive = 1")
    suspend fun getTotalRemindersCount(): Int
    
    @Query("SELECT COUNT(*) FROM islamic_reminders WHERE type = 'QURAN' AND isActive = 1")
    suspend fun getQuranVersesCount(): Int
    
    @Query("SELECT COUNT(*) FROM islamic_reminders WHERE type = 'HADITH' AND isActive = 1")
    suspend fun getHadithCount(): Int
    
    @Query("SELECT COUNT(*) FROM islamic_reminders WHERE type = 'DUA' AND isActive = 1")
    suspend fun getDuasCount(): Int
    
    @Query("SELECT COUNT(*) FROM islamic_reminders WHERE isFavorite = 1 AND isActive = 1")
    suspend fun getFavoriteCount(): Int
    
    @Query("SELECT category, COUNT(*) as count FROM islamic_reminders WHERE isActive = 1 GROUP BY category ORDER BY count DESC LIMIT 1")
    suspend fun getMostShownCategory(): CategoryUsageCount?
    
    @Query("SELECT SUM(usageCount) FROM islamic_reminders WHERE isActive = 1")
    suspend fun getTotalUsage(): Int?
    
    @Query("SELECT MAX(lastShown) FROM islamic_reminders WHERE isActive = 1")
    suspend fun getLastShownTime(): Long?
    
    @Query("DELETE FROM islamic_reminders")
    suspend fun clearAllReminders()
    
    @Query("UPDATE islamic_reminders SET isActive = 0 WHERE id = :id")
    suspend fun deactivateReminder(id: Int)
    
    @Query("UPDATE islamic_reminders SET isActive = 1 WHERE id = :id")
    suspend fun activateReminder(id: Int)
}

/**
 * Data class for category usage count query result
 */
data class CategoryUsageCount(
    val category: String,
    val count: Int
)