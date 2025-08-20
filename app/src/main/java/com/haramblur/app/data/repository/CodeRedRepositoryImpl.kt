package com.haramblur.app.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.haramblur.app.data.local.dao.CodeRedSessionDao
import com.haramblur.app.data.local.entity.toDomain
import com.haramblur.app.data.local.entity.toEntity
import com.haramblur.app.domain.model.*
import com.haramblur.app.domain.repository.CodeRedRepository
import com.haramblur.app.domain.repository.CodeRedStats
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CodeRedRepository
 */
@Singleton
class CodeRedRepositoryImpl @Inject constructor(
    private val codeRedSessionDao: CodeRedSessionDao,
    @ApplicationContext private val context: Context
) : CodeRedRepository {
    
    override suspend fun createSession(session: CodeRedSession) {
        codeRedSessionDao.insertSession(session.toEntity())
    }
    
    override suspend fun updateSession(session: CodeRedSession) {
        codeRedSessionDao.updateSession(session.toEntity())
    }
    
    override suspend fun getActiveSession(): CodeRedSession? {
        return codeRedSessionDao.getActiveSession()?.toDomain()
    }
    
    override fun getActiveSessionFlow(): Flow<CodeRedSession?> {
        return codeRedSessionDao.getActiveSessionFlow().map { entity ->
            entity?.toDomain()
        }
    }
    
    override suspend fun getSession(sessionId: String): CodeRedSession? {
        return codeRedSessionDao.getSessionById(sessionId)?.toDomain()
    }
    
    override fun getAllSessions(): Flow<List<CodeRedSession>> {
        return codeRedSessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getSessionsByStatus(status: SessionStatus): Flow<List<CodeRedSession>> {
        return codeRedSessionDao.getSessionsByStatus(status.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun terminateActiveSession(reason: String?) {
        codeRedSessionDao.terminateActiveSessions()
    }
    
    override suspend fun incrementUnlockAttempts(sessionId: String) {
        codeRedSessionDao.incrementUnlockAttempts(sessionId, System.currentTimeMillis())
    }
    
    override suspend fun getLockableApps(): List<LockableApp> {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        return installedApps
            .filter { appInfo ->
                // Filter out system apps and this app itself
                !appInfo.packageName.equals(context.packageName) &&
                (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0
            }
            .map { appInfo ->
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val category = categorizeApp(appInfo.packageName)
                
                LockableApp(
                    packageName = appInfo.packageName,
                    appName = appName,
                    category = category,
                    isSystemApp = false
                )
            }
            .sortedBy { it.appName }
    }
    
    override suspend fun getSessionStats(): CodeRedStats {
        val totalSessions = codeRedSessionDao.getTotalSessionsCount()
        val activeSessions = codeRedSessionDao.getActiveSessionsCount()
        val completedSessions = codeRedSessionDao.getCompletedSessionsCount()
        val terminatedSessions = codeRedSessionDao.getTerminatedSessionsCount()
        val averageSessionDuration = codeRedSessionDao.getAverageSessionDuration() ?: 0L
        val totalTimeLockedMs = codeRedSessionDao.getTotalTimeLockedMs() ?: 0L
        val successfulCompletions = codeRedSessionDao.getSuccessfulCompletions()
        val lastSessionTime = codeRedSessionDao.getLastSessionTime()
        
        // Get most locked app pattern
        val mostLockedPattern = codeRedSessionDao.getMostLockedAppsPattern()
        val mostLockedApp = mostLockedPattern?.let { pattern ->
            // Parse the JSON to find the most common app
            val apps = pattern.lockedAppsJson.split(",")
            apps.firstOrNull()
        }
        
        return CodeRedStats(
            totalSessions = totalSessions,
            activeSessions = activeSessions,
            completedSessions = completedSessions,
            terminatedSessions = terminatedSessions,
            averageSessionDuration = averageSessionDuration,
            totalTimeLockedMs = totalTimeLockedMs,
            mostLockedApp = mostLockedApp,
            successfulCompletions = successfulCompletions,
            lastSessionTime = lastSessionTime
        )
    }
    
    override suspend fun cleanupOldSessions(retentionDays: Int) {
        val cutoffTime = System.currentTimeMillis() - (retentionDays * 24 * 60 * 60 * 1000L)
        codeRedSessionDao.deleteOldSessions(cutoffTime)
    }
    
    /**
     * Categorize app based on package name
     */
    private fun categorizeApp(packageName: String): AppCategory {
        return when {
            isSocialMediaApp(packageName) -> AppCategory.SOCIAL_MEDIA
            isEntertainmentApp(packageName) -> AppCategory.ENTERTAINMENT
            isDatingApp(packageName) -> AppCategory.DATING
            isGamingApp(packageName) -> AppCategory.GAMING
            isBrowserApp(packageName) -> AppCategory.BROWSER
            isMessagingApp(packageName) -> AppCategory.MESSAGING
            else -> AppCategory.OTHER
        }
    }
    
    private fun isSocialMediaApp(packageName: String): Boolean {
        val socialMediaPackages = listOf(
            "com.facebook.katana", "com.instagram.android", "com.twitter.android",
            "com.snapchat.android", "com.linkedin.android", "com.pinterest",
            "com.zhiliaoapp.musically", "com.ss.android.ugc.trill" // TikTok
        )
        return socialMediaPackages.any { packageName.contains(it, ignoreCase = true) }
    }
    
    private fun isEntertainmentApp(packageName: String): Boolean {
        val entertainmentPackages = listOf(
            "com.google.android.youtube", "com.netflix.mediaclient", 
            "tv.twitch.android.app", "com.spotify.music", "com.reddit.frontpage"
        )
        return entertainmentPackages.any { packageName.contains(it, ignoreCase = true) }
    }
    
    private fun isDatingApp(packageName: String): Boolean {
        val datingPackages = listOf(
            "com.tinder", "com.bumble.app", "com.match.android",
            "com.okcupid.okcupid", "com.pof.android"
        )
        return datingPackages.any { packageName.contains(it, ignoreCase = true) }
    }
    
    private fun isGamingApp(packageName: String): Boolean {
        return packageName.contains("game", ignoreCase = true) ||
                packageName.contains("play", ignoreCase = true)
    }
    
    private fun isBrowserApp(packageName: String): Boolean {
        val browserPackages = listOf(
            "com.android.chrome", "org.mozilla.firefox", "com.microsoft.emmx",
            "com.opera.browser", "com.brave.browser"
        )
        return browserPackages.any { packageName.contains(it, ignoreCase = true) }
    }
    
    private fun isMessagingApp(packageName: String): Boolean {
        val messagingPackages = listOf(
            "com.whatsapp", "org.telegram.messenger", "com.viber.voip",
            "com.skype.raider", "com.discord"
        )
        return messagingPackages.any { packageName.contains(it, ignoreCase = true) }
    }
}