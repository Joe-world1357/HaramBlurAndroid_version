package com.haramblur.app.data.repositories

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.haramblur.app.data.dao.LockPresetDao
import com.haramblur.app.data.dao.LockSessionDao
import com.haramblur.app.data.models.AppInfo
import com.haramblur.app.data.models.LockPreset
import com.haramblur.app.data.models.LockSession
import com.haramblur.app.data.models.entities.toDomainModel
import com.haramblur.app.data.models.entities.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for CODE RED app locking functionality
 */
class CodeRedRepository(
    private val lockSessionDao: LockSessionDao,
    private val lockPresetDao: LockPresetDao,
    private val context: Context
) {

    /**
     * Start a new lock session
     */
    suspend fun startLockSession(apps: List<String>, durationMinutes: Int, reason: String): Long {
        val endTime = if (durationMinutes > 0) {
            System.currentTimeMillis() + (durationMinutes * 60 * 1000)
        } else {
            0 // 0 means indefinite
        }
        
        val session = LockSession(
            startTime = System.currentTimeMillis(),
            endTime = endTime,
            lockedApps = apps,
            reason = reason,
            isActive = true
        )
        
        return lockSessionDao.insert(session.toEntity())
    }

    /**
     * End a lock session
     */
    suspend fun endLockSession(sessionId: Long) {
        lockSessionDao.endSession(sessionId)
    }

    /**
     * End all active lock sessions
     */
    suspend fun endAllLockSessions() {
        lockSessionDao.endAllActiveSessions()
    }

    /**
     * Get all active lock sessions
     */
    fun getActiveSessions(): Flow<List<LockSession>> {
        return lockSessionDao.getActiveSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Get lock session history
     */
    fun getLockHistory(): Flow<List<LockSession>> {
        return lockSessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Get all lock presets
     */
    fun getLockPresets(): Flow<List<LockPreset>> {
        return lockPresetDao.getAllPresets().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Create a new lock preset
     */
    suspend fun createLockPreset(name: String, apps: List<String>, durationMinutes: Int): Long {
        val preset = LockPreset(
            name = name,
            apps = apps,
            durationMinutes = durationMinutes
        )
        return lockPresetDao.insert(preset.toEntity())
    }

    /**
     * Delete a lock preset
     */
    suspend fun deleteLockPreset(presetId: Long) {
        val preset = lockPresetDao.getById(presetId)
        preset?.let {
            lockPresetDao.delete(it)
        }
    }

    /**
     * Get all installed apps
     */
    fun getInstalledApps(): List<AppInfo> {
        val pm = context.packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        
        return installedApps.map { appInfo ->
            val appName = try {
                pm.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                appInfo.packageName
            }
            
            AppInfo(
                packageName = appInfo.packageName,
                appName = appName,
                isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                isLocked = false // Default to not locked
            )
        }.filter {
            // Filter out system apps that shouldn't be shown
            !it.isSystemApp || it.packageName.contains("browser") || 
                    it.packageName.contains("chrome") || 
                    it.packageName.contains("youtube")
        }.sortedBy { it.appName }
    }

    /**
     * Check if an app is currently locked
     */
    suspend fun isAppLocked(packageName: String): Boolean {
        val activeSessions = lockSessionDao.getActiveSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }.map { sessions ->
            sessions.filter { session ->
                val isExpired = session.endTime > 0 && System.currentTimeMillis() > session.endTime
                !isExpired
            }
        }
        
        return activeSessions.map { sessions ->
            sessions.any { session -> session.lockedApps.contains(packageName) }
        }.map { isLocked -> isLocked }.first()
    }
}
