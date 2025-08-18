package com.haramblur.app.domain.usecase.codered

import com.haramblur.app.domain.model.CodeRedSession
import com.haramblur.app.domain.repository.CodeRedRepository
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Use case for creating a new CODE RED session
 */
class CreateCodeRedSessionUseCase @Inject constructor(
    private val codeRedRepository: CodeRedRepository
) {
    
    /**
     * Create a new CODE RED session with app locking
     */
    suspend fun execute(
        lockedApps: List<String>,
        durationMinutes: Int,
        password: String,
        reason: String? = null
    ): Result<CodeRedSession> {
        
        return try {
            // Check if there's already an active session
            val activeSession = codeRedRepository.getActiveSession()
            if (activeSession != null) {
                return Result.failure(IllegalStateException("Active session already exists"))
            }
            
            // Validate inputs
            if (lockedApps.isEmpty()) {
                return Result.failure(IllegalArgumentException("No apps selected for locking"))
            }
            
            if (durationMinutes <= 0) {
                return Result.failure(IllegalArgumentException("Duration must be positive"))
            }
            
            if (password.length < 8) {
                return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
            }
            
            // Create session
            val startTime = System.currentTimeMillis()
            val endTime = startTime + (durationMinutes * 60 * 1000L)
            
            val session = CodeRedSession(
                startTime = startTime,
                endTime = endTime,
                lockedApps = lockedApps,
                passwordHash = hashPassword(password),
                reason = reason
            )
            
            // Save session
            codeRedRepository.createSession(session)
            
            Result.success(session)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Hash password securely
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Validate password strength
     */
    fun validatePasswordStrength(password: String): PasswordStrength {
        return when {
            password.length < 8 -> PasswordStrength.WEAK
            password.length < 12 -> PasswordStrength.FAIR
            password.length >= 12 && containsSpecialChars(password) -> PasswordStrength.STRONG
            else -> PasswordStrength.FAIR
        }
    }
    
    /**
     * Check if password contains special characters
     */
    private fun containsSpecialChars(password: String): Boolean {
        val specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"
        return password.any { it in specialChars } &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
    }
}

/**
 * Password strength levels
 */
enum class PasswordStrength {
    WEAK,
    FAIR,
    STRONG
}