package com.haramblur.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.domain.model.CodeRedSession
import com.haramblur.app.domain.model.SessionStatus

/**
 * Room entity for CODE RED sessions
 */
@Entity(tableName = "code_red_sessions")
data class CodeRedSessionEntity(
    @PrimaryKey
    val sessionId: String,
    val startTime: Long,
    val endTime: Long,
    val lockedAppsJson: String, // JSON string of app package names
    val passwordHash: String,
    val reason: String?,
    val status: String,
    val unlockAttempts: Int,
    val lastUnlockAttempt: Long?
)

/**
 * Convert domain model to entity
 */
fun CodeRedSession.toEntity(): CodeRedSessionEntity {
    return CodeRedSessionEntity(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        lockedAppsJson = lockedApps.joinToString(","),
        passwordHash = passwordHash,
        reason = reason,
        status = status.name,
        unlockAttempts = unlockAttempts,
        lastUnlockAttempt = lastUnlockAttempt
    )
}

/**
 * Convert entity to domain model
 */
fun CodeRedSessionEntity.toDomain(): CodeRedSession {
    return CodeRedSession(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        lockedApps = if (lockedAppsJson.isBlank()) emptyList() else lockedAppsJson.split(","),
        passwordHash = passwordHash,
        reason = reason,
        status = SessionStatus.valueOf(status),
        unlockAttempts = unlockAttempts,
        lastUnlockAttempt = lastUnlockAttempt
    )
}