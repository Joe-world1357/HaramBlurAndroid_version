package com.haramblur.app.data.local.dao

import androidx.room.*
import com.haramblur.app.data.local.entity.CodeRedSessionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CODE RED sessions
 */
@Dao
interface CodeRedSessionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: CodeRedSessionEntity)
    
    @Update
    suspend fun updateSession(session: CodeRedSessionEntity)
    
    @Delete
    suspend fun deleteSession(session: CodeRedSessionEntity)
    
    @Query("DELETE FROM code_red_sessions WHERE sessionId = :sessionId")
    suspend fun deleteSessionById(sessionId: String)
    
    @Query("SELECT * FROM code_red_sessions WHERE status = 'ACTIVE' LIMIT 1")
    suspend fun getActiveSession(): CodeRedSessionEntity?
    
    @Query("SELECT * FROM code_red_sessions WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveSessionFlow(): Flow<CodeRedSessionEntity?>
    
    @Query("SELECT * FROM code_red_sessions WHERE sessionId = :sessionId")
    suspend fun getSessionById(sessionId: String): CodeRedSessionEntity?
    
    @Query("SELECT * FROM code_red_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<CodeRedSessionEntity>>
    
    @Query("SELECT * FROM code_red_sessions WHERE status = :status ORDER BY startTime DESC")
    fun getSessionsByStatus(status: String): Flow<List<CodeRedSessionEntity>>
    
    @Query("UPDATE code_red_sessions SET status = 'TERMINATED' WHERE status = 'ACTIVE'")
    suspend fun terminateActiveSessions()
    
    @Query("UPDATE code_red_sessions SET unlockAttempts = unlockAttempts + 1, lastUnlockAttempt = :timestamp WHERE sessionId = :sessionId")
    suspend fun incrementUnlockAttempts(sessionId: String, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM code_red_sessions")
    suspend fun getTotalSessionsCount(): Int
    
    @Query("SELECT COUNT(*) FROM code_red_sessions WHERE status = 'ACTIVE'")
    suspend fun getActiveSessionsCount(): Int
    
    @Query("SELECT COUNT(*) FROM code_red_sessions WHERE status = 'COMPLETED'")
    suspend fun getCompletedSessionsCount(): Int
    
    @Query("SELECT COUNT(*) FROM code_red_sessions WHERE status = 'TERMINATED'")
    suspend fun getTerminatedSessionsCount(): Int
    
    @Query("SELECT AVG(endTime - startTime) FROM code_red_sessions WHERE status IN ('COMPLETED', 'TERMINATED')")
    suspend fun getAverageSessionDuration(): Long?
    
    @Query("SELECT SUM(endTime - startTime) FROM code_red_sessions WHERE status IN ('COMPLETED', 'TERMINATED')")
    suspend fun getTotalTimeLockedMs(): Long?
    
    @Query("""
        SELECT lockedAppsJson, COUNT(*) as count 
        FROM code_red_sessions 
        WHERE status IN ('COMPLETED', 'TERMINATED') 
        GROUP BY lockedAppsJson 
        ORDER BY count DESC 
        LIMIT 1
    """)
    suspend fun getMostLockedAppsPattern(): AppLockPattern?
    
    @Query("SELECT COUNT(*) FROM code_red_sessions WHERE status = 'COMPLETED'")
    suspend fun getSuccessfulCompletions(): Int
    
    @Query("SELECT MAX(startTime) FROM code_red_sessions")
    suspend fun getLastSessionTime(): Long?
    
    @Query("DELETE FROM code_red_sessions WHERE endTime < :cutoffTime AND status IN ('COMPLETED', 'TERMINATED')")
    suspend fun deleteOldSessions(cutoffTime: Long): Int
    
    @Query("DELETE FROM code_red_sessions")
    suspend fun clearAllSessions()
    
    @Query("""
        UPDATE code_red_sessions 
        SET status = 'COMPLETED' 
        WHERE status = 'ACTIVE' AND endTime <= :currentTime
    """)
    suspend fun markExpiredSessionsAsCompleted(currentTime: Long)
}

/**
 * Data class for app lock pattern query result
 */
data class AppLockPattern(
    val lockedAppsJson: String,
    val count: Int
)