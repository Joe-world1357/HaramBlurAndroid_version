package com.haramblur.app.domain.usecase.codered

import com.haramblur.app.domain.model.CodeRedSession
import com.haramblur.app.domain.model.SessionStatus
import com.haramblur.app.domain.repository.CodeRedRepository
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Use case for managing active CODE RED sessions
 */
class ManageCodeRedSessionUseCase @Inject constructor(
    private val codeRedRepository: CodeRedRepository
) {
    
    /**
     * Get the active session as a flow
     */
    fun getActiveSession(): Flow<CodeRedSession?> {
        return codeRedRepository.getActiveSessionFlow()
    }
    
    /**
     * Check if an app is currently locked
     */
    suspend fun isAppLocked(packageName: String): Boolean {
        val activeSession = codeRedRepository.getActiveSession()
        return activeSession?.status == SessionStatus.ACTIVE && 
                packageName in activeSession.lockedApps
    }
    
    /**
     * Attempt to unlock session with password
     */
    suspend fun unlockSession(sessionId: String, password: String): UnlockResult {
        return try {
            val session = codeRedRepository.getSession(sessionId)
                ?: return UnlockResult.SessionNotFound
            
            if (session.status != SessionStatus.ACTIVE) {
                return UnlockResult.SessionNotActive
            }
            
            // Check if session has expired
            val currentTime = System.currentTimeMillis()
            if (currentTime >= session.endTime) {
                // Mark as completed
                val completedSession = session.copy(status = SessionStatus.COMPLETED)
                codeRedRepository.updateSession(completedSession)
                return UnlockResult.SessionExpired
            }
            
            // Verify password
            val hashedPassword = hashPassword(password)
            if (hashedPassword != session.passwordHash) {
                // Increment unlock attempts
                codeRedRepository.incrementUnlockAttempts(sessionId)
                return UnlockResult.WrongPassword
            }
            
            // Terminate session
            val terminatedSession = session.copy(
                status = SessionStatus.TERMINATED,
                lastUnlockAttempt = currentTime
            )
            codeRedRepository.updateSession(terminatedSession)
            
            UnlockResult.Success
            
        } catch (e: Exception) {
            UnlockResult.Error(e.message ?: "Unknown error")
        }
    }
    
    /**
     * Get remaining time for active session
     */
    suspend fun getRemainingTime(): Long {
        val activeSession = codeRedRepository.getActiveSession()
        return if (activeSession != null && activeSession.status == SessionStatus.ACTIVE) {
            maxOf(0, activeSession.endTime - System.currentTimeMillis())
        } else {
            0L
        }
    }
    
    /**
     * Check and update expired sessions
     */
    suspend fun updateExpiredSessions() {
        val activeSession = codeRedRepository.getActiveSession()
        if (activeSession != null && 
            activeSession.status == SessionStatus.ACTIVE &&
            System.currentTimeMillis() >= activeSession.endTime) {
            
            val completedSession = activeSession.copy(status = SessionStatus.COMPLETED)
            codeRedRepository.updateSession(completedSession)
        }
    }
    
    /**
     * Emergency terminate session (for testing or emergency situations)
     */
    suspend fun emergencyTerminate(reason: String = "Emergency termination"): Boolean {
        return try {
            codeRedRepository.terminateActiveSession(reason)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Hash password for comparison
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}

/**
 * Result of unlock attempt
 */
sealed class UnlockResult {
    object Success : UnlockResult()
    object SessionNotFound : UnlockResult()
    object SessionNotActive : UnlockResult()
    object SessionExpired : UnlockResult()
    object WrongPassword : UnlockResult()
    data class Error(val message: String) : UnlockResult()
}