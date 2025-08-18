package com.haramblur.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haramblur.app.data.models.entities.LockSessionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for lock sessions
 */
@Dao
interface LockSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: LockSessionEntity): Long

    @Update
    suspend fun update(session: LockSessionEntity)

    @Delete
    suspend fun delete(session: LockSessionEntity)

    @Query("SELECT * FROM lock_sessions WHERE id = :id")
    suspend fun getById(id: Long): LockSessionEntity?

    @Query("SELECT * FROM lock_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<LockSessionEntity>>

    @Query("SELECT * FROM lock_sessions WHERE isActive = 1")
    fun getActiveSessions(): Flow<List<LockSessionEntity>>

    @Query("SELECT * FROM lock_sessions WHERE startTime BETWEEN :startTime AND :endTime ORDER BY startTime DESC")
    fun getSessionsBetween(startTime: Long, endTime: Long): Flow<List<LockSessionEntity>>

    @Query("UPDATE lock_sessions SET isActive = 0, endTime = :endTime WHERE id = :id")
    suspend fun endSession(id: Long, endTime: Long = System.currentTimeMillis())

    @Query("UPDATE lock_sessions SET isActive = 0, endTime = :endTime WHERE isActive = 1")
    suspend fun endAllActiveSessions(endTime: Long = System.currentTimeMillis())
}
