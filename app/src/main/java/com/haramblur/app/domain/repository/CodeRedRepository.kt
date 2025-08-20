package com.haramblur.app.domain.repository

import com.haramblur.app.domain.model.CodeRedSession
import com.haramblur.app.domain.model.LockableApp
import com.haramblur.app.domain.model.SessionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for CODE RED session operations
 */
interface CodeRedRepository {
    
    /**
     * Create a new CODE RED session
     */
    suspend fun createSession(session: CodeRedSession)
    
    /**
     * Update an existing session
     */
    suspend fun updateSession(session: CodeRedSession)
    
    /**
     * Get active session
     */
    suspend fun getActiveSession(): CodeRedSession?
    
    /**
     * Get active session as flow
     */
    fun getActiveSessionFlow(): Flow<CodeRedSession?>
    
    /**
     * Get session by ID
     */
    suspend fun getSession(sessionId: String): CodeRedSession?
    
    /**
     * Get all sessions
     */
    fun getAllSessions(): Flow<List<CodeRedSession>>
    
    /**
     * Get sessions by status
     */
    fun getSessionsByStatus(status: SessionStatus): Flow<List<CodeRedSession>>
    
    /**
     * Terminate active session
     */
    suspend fun terminateActiveSession(reason: String? = null)
    
    /**
     * Update session unlock attempts
     */
    suspend fun incrementUnlockAttempts(sessionId: String)
    
    /**
     * Get all installed lockable apps
     */
    suspend fun getLockableApps(): List<LockableApp>
    
    /**
     * Get session statistics
     */
    suspend fun getSessionStats(): CodeRedStats
    
    /**
     * Clean up old completed sessions
     */
    suspend fun cleanupOldSessions(retentionDays: Int)
}

/**
 * Statistics for CODE RED sessions
 */
data class CodeRedStats(
    val totalSessions: Int = 0,
    val activeSessions: Int = 0,
    val completedSessions: Int = 0,
    val terminatedSessions: Int = 0,
    val averageSessionDuration: Long = 0L,
    val totalTimeLockedMs: Long = 0L,
    val mostLockedApp: String? = null,
    val successfulCompletions: Int = 0,
    val lastSessionTime: Long? = null
)