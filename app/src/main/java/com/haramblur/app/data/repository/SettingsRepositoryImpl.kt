package com.haramblur.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.haramblur.app.domain.model.*
import com.haramblur.app.domain.repository.SettingsRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SettingsRepository using DataStore
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    
    private object PreferencesKeys {
        val PROTECTION_ENABLED = booleanPreferencesKey("protection_enabled")
        val DETECTION_SENSITIVITY = floatPreferencesKey("detection_sensitivity")
        val PROCESSING_FRAME_RATE = intPreferencesKey("processing_frame_rate")
        val BLUR_TYPE = stringPreferencesKey("blur_type")
        val BLUR_INTENSITY = stringPreferencesKey("blur_intensity")
        val ENABLE_WEBSITE_BLOCKING = booleanPreferencesKey("enable_website_blocking")
        val ENABLE_ISLAMIC_REMINDERS = booleanPreferencesKey("enable_islamic_reminders")
        val REMINDER_FREQUENCY = stringPreferencesKey("reminder_frequency")
        val PREFERRED_LANGUAGE = stringPreferencesKey("preferred_language")
        val ENABLE_ARABIC_TEXT = booleanPreferencesKey("enable_arabic_text")
        val DATA_RETENTION_DAYS = intPreferencesKey("data_retention_days")
        val PERFORMANCE_MODE = stringPreferencesKey("performance_mode")
        val ENABLE_NOTIFICATIONS = booleanPreferencesKey("enable_notifications")
        val ENABLE_VIBRATION = booleanPreferencesKey("enable_vibration")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }
    
    override fun getSettings(): Flow<AppSettings> {
        return context.dataStore.data.map { preferences ->
            AppSettings(
                protectionEnabled = preferences[PreferencesKeys.PROTECTION_ENABLED] ?: true,
                detectionSensitivity = preferences[PreferencesKeys.DETECTION_SENSITIVITY] ?: 0.8f,
                processingFrameRate = preferences[PreferencesKeys.PROCESSING_FRAME_RATE] ?: 3,
                blurType = preferences[PreferencesKeys.BLUR_TYPE]?.let { 
                    try { BlurType.valueOf(it) } catch (e: Exception) { BlurType.GAUSSIAN }
                } ?: BlurType.GAUSSIAN,
                blurIntensity = preferences[PreferencesKeys.BLUR_INTENSITY]?.let {
                    try { BlurIntensity.valueOf(it) } catch (e: Exception) { BlurIntensity.MEDIUM }
                } ?: BlurIntensity.MEDIUM,
                enableWebsiteBlocking = preferences[PreferencesKeys.ENABLE_WEBSITE_BLOCKING] ?: true,
                enableIslamicReminders = preferences[PreferencesKeys.ENABLE_ISLAMIC_REMINDERS] ?: true,
                reminderFrequency = preferences[PreferencesKeys.REMINDER_FREQUENCY]?.let {
                    try { ReminderFrequency.valueOf(it) } catch (e: Exception) { ReminderFrequency.NORMAL }
                } ?: ReminderFrequency.NORMAL,
                preferredLanguage = preferences[PreferencesKeys.PREFERRED_LANGUAGE] ?: "en",
                enableArabicText = preferences[PreferencesKeys.ENABLE_ARABIC_TEXT] ?: true,
                dataRetentionDays = preferences[PreferencesKeys.DATA_RETENTION_DAYS] ?: 30,
                performanceMode = preferences[PreferencesKeys.PERFORMANCE_MODE]?.let {
                    try { PerformanceMode.valueOf(it) } catch (e: Exception) { PerformanceMode.BALANCED }
                } ?: PerformanceMode.BALANCED,
                enableNotifications = preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] ?: true,
                enableVibration = preferences[PreferencesKeys.ENABLE_VIBRATION] ?: true,
                themeMode = preferences[PreferencesKeys.THEME_MODE]?.let {
                    try { ThemeMode.valueOf(it) } catch (e: Exception) { ThemeMode.SYSTEM }
                } ?: ThemeMode.SYSTEM
            )
        }
    }
    
    override suspend fun getCurrentSettings(): AppSettings {
        return getSettings().first()
    }
    
    override suspend fun updateSettings(settings: AppSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROTECTION_ENABLED] = settings.protectionEnabled
            preferences[PreferencesKeys.DETECTION_SENSITIVITY] = settings.detectionSensitivity
            preferences[PreferencesKeys.PROCESSING_FRAME_RATE] = settings.processingFrameRate
            preferences[PreferencesKeys.BLUR_TYPE] = settings.blurType.name
            preferences[PreferencesKeys.BLUR_INTENSITY] = settings.blurIntensity.name
            preferences[PreferencesKeys.ENABLE_WEBSITE_BLOCKING] = settings.enableWebsiteBlocking
            preferences[PreferencesKeys.ENABLE_ISLAMIC_REMINDERS] = settings.enableIslamicReminders
            preferences[PreferencesKeys.REMINDER_FREQUENCY] = settings.reminderFrequency.name
            preferences[PreferencesKeys.PREFERRED_LANGUAGE] = settings.preferredLanguage
            preferences[PreferencesKeys.ENABLE_ARABIC_TEXT] = settings.enableArabicText
            preferences[PreferencesKeys.DATA_RETENTION_DAYS] = settings.dataRetentionDays
            preferences[PreferencesKeys.PERFORMANCE_MODE] = settings.performanceMode.name
            preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] = settings.enableNotifications
            preferences[PreferencesKeys.ENABLE_VIBRATION] = settings.enableVibration
            preferences[PreferencesKeys.THEME_MODE] = settings.themeMode.name
        }
    }
    
    override suspend fun updateProtectionEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROTECTION_ENABLED] = enabled
        }
    }
    
    override suspend fun updateDetectionSensitivity(sensitivity: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DETECTION_SENSITIVITY] = sensitivity
        }
    }
    
    override suspend fun updateBlurType(blurType: BlurType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BLUR_TYPE] = blurType.name
        }
    }
    
    override suspend fun updateBlurIntensity(intensity: BlurIntensity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BLUR_INTENSITY] = intensity.name
        }
    }
    
    override suspend fun updateWebsiteBlocking(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_WEBSITE_BLOCKING] = enabled
        }
    }
    
    override suspend fun updateIslamicReminders(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_ISLAMIC_REMINDERS] = enabled
        }
    }
    
    override suspend fun updateReminderFrequency(frequency: ReminderFrequency) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_FREQUENCY] = frequency.name
        }
    }
    
    override suspend fun updatePerformanceMode(mode: PerformanceMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PERFORMANCE_MODE] = mode.name
        }
    }
    
    override suspend fun updateThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }
    
    override suspend fun resetToDefaults() {
        updateSettings(AppSettings())
    }
    
    override suspend fun exportSettings(): String {
        val settings = getCurrentSettings()
        return Gson().toJson(settings)
    }
    
    override suspend fun importSettings(settingsJson: String): Boolean {
        return try {
            val settings = Gson().fromJson(settingsJson, AppSettings::class.java)
            updateSettings(settings)
            true
        } catch (e: Exception) {
            false
        }
    }
}