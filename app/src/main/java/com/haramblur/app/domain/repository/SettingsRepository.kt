package com.haramblur.app.domain.repository

import com.haramblur.app.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for app settings and preferences
 */
interface SettingsRepository {
    
    /**
     * Get app settings as flow
     */
    fun getSettings(): Flow<AppSettings>
    
    /**
     * Get current app settings
     */
    suspend fun getCurrentSettings(): AppSettings
    
    /**
     * Update app settings
     */
    suspend fun updateSettings(settings: AppSettings)
    
    /**
     * Update specific setting
     */
    suspend fun updateProtectionEnabled(enabled: Boolean)
    suspend fun updateDetectionSensitivity(sensitivity: Float)
    suspend fun updateBlurType(blurType: com.haramblur.app.domain.model.BlurType)
    suspend fun updateBlurIntensity(intensity: com.haramblur.app.domain.model.BlurIntensity)
    suspend fun updateWebsiteBlocking(enabled: Boolean)
    suspend fun updateIslamicReminders(enabled: Boolean)
    suspend fun updateReminderFrequency(frequency: com.haramblur.app.domain.model.ReminderFrequency)
    suspend fun updatePerformanceMode(mode: com.haramblur.app.domain.model.PerformanceMode)
    suspend fun updateThemeMode(mode: com.haramblur.app.domain.model.ThemeMode)
    
    /**
     * Reset settings to defaults
     */
    suspend fun resetToDefaults()
    
    /**
     * Export settings for backup
     */
    suspend fun exportSettings(): String
    
    /**
     * Import settings from backup
     */
    suspend fun importSettings(settingsJson: String): Boolean
}