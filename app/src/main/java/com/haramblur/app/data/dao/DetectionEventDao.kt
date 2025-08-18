package com.haramblur.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haramblur.app.data.models.entities.DetectionEventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for detection events
 */
@Dao
interface DetectionEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: DetectionEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<DetectionEventEntity>)

    @Update
    suspend fun update(event: DetectionEventEntity)

    @Delete
    suspend fun delete(event: DetectionEventEntity)

    @Query("DELETE FROM detection_events")
    suspend fun deleteAll()

    @Query("SELECT * FROM detection_events WHERE id = :id")
    suspend fun getById(id: Long): DetectionEventEntity?

    @Query("SELECT * FROM detection_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<DetectionEventEntity>>

    @Query("SELECT * FROM detection_events WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getEventsBetween(startTime: Long, endTime: Long): Flow<List<DetectionEventEntity>>

    @Query("SELECT COUNT(*) FROM detection_events WHERE timestamp >= :startTime")
    fun getEventCountSince(startTime: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM detection_events WHERE contentType = :contentType AND timestamp >= :startTime")
    fun getEventCountByTypeSince(contentType: String, startTime: Long): Flow<Int>
}
