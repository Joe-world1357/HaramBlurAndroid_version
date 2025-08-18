package com.haramblur.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haramblur.app.data.models.entities.IslamicReminderEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Islamic reminders
 */
@Dao
interface IslamicReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: IslamicReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reminders: List<IslamicReminderEntity>)

    @Update
    suspend fun update(reminder: IslamicReminderEntity)

    @Delete
    suspend fun delete(reminder: IslamicReminderEntity)

    @Query("SELECT * FROM islamic_reminders WHERE id = :id")
    suspend fun getById(id: Long): IslamicReminderEntity?

    @Query("SELECT * FROM islamic_reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<IslamicReminderEntity>>

    @Query("SELECT * FROM islamic_reminders WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomReminderByCategory(category: String): IslamicReminderEntity?

    @Query("SELECT * FROM islamic_reminders ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomReminder(): IslamicReminderEntity?

    @Query("SELECT * FROM islamic_reminders WHERE category = :category")
    fun getRemindersByCategory(category: String): Flow<List<IslamicReminderEntity>>
}
