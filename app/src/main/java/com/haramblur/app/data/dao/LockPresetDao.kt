package com.haramblur.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haramblur.app.data.models.entities.LockPresetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for lock presets
 */
@Dao
interface LockPresetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: LockPresetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(presets: List<LockPresetEntity>)

    @Update
    suspend fun update(preset: LockPresetEntity)

    @Delete
    suspend fun delete(preset: LockPresetEntity)

    @Query("SELECT * FROM lock_presets WHERE id = :id")
    suspend fun getById(id: Long): LockPresetEntity?

    @Query("SELECT * FROM lock_presets ORDER BY name ASC")
    fun getAllPresets(): Flow<List<LockPresetEntity>>

    @Query("SELECT * FROM lock_presets WHERE name LIKE '%' || :search || '%'")
    fun searchPresets(search: String): Flow<List<LockPresetEntity>>
}
