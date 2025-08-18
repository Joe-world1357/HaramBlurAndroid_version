package com.haramblur.app.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen
 */
class SettingsViewModel(private val context: Context) : ViewModel() {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("haramblur_settings", Context.MODE_PRIVATE)
    }

    // Content detection settings
    private val _detectFemaleFaces = MutableStateFlow(true)
    val detectFemaleFaces: StateFlow<Boolean> = _detectFemaleFaces.asStateFlow()

    private val _detectNsfw = MutableStateFlow(true)
    val detectNsfw: StateFlow<Boolean> = _detectNsfw.asStateFlow()

    private val _detectNudity = MutableStateFlow(true)
    val detectNudity: StateFlow<Boolean> = _detectNudity.asStateFlow()

    private val _detectSuggestive = MutableStateFlow(false)
    val detectSuggestive: StateFlow<Boolean> = _detectSuggestive.asStateFlow()

    // Sensitivity level (0-100)
    private val _sensitivityLevel = MutableStateFlow(50)
    val sensitivityLevel: StateFlow<Int> = _sensitivityLevel.asStateFlow()

    // Blur effect type (0: Pixelate, 1: Blur, 2: Black)
    private val _blurEffectType = MutableStateFlow(1)
    val blurEffectType: StateFlow<Int> = _blurEffectType.asStateFlow()

    // Reminder settings
    private val _reminderFrequency = MutableStateFlow(2) // 0: None, 1: Rare, 2: Moderate, 3: Frequent
    val reminderFrequency: StateFlow<Int> = _reminderFrequency.asStateFlow()

    // Theme settings
    private val _useDarkTheme = MutableStateFlow(false) // 0: System, 1: Light, 2: Dark
    val useDarkTheme: StateFlow<Boolean> = _useDarkTheme.asStateFlow()

    init {
        loadSettings()
    }

    /**
     * Load settings from SharedPreferences
     */
    private fun loadSettings() {
        viewModelScope.launch {
            _detectFemaleFaces.value = preferences.getBoolean("detect_female_faces", true)
            _detectNsfw.value = preferences.getBoolean("detect_nsfw", true)
            _detectNudity.value = preferences.getBoolean("detect_nudity", true)
            _detectSuggestive.value = preferences.getBoolean("detect_suggestive", false)
            _sensitivityLevel.value = preferences.getInt("sensitivity_level", 50)
            _blurEffectType.value = preferences.getInt("blur_effect_type", 1)
            _reminderFrequency.value = preferences.getInt("reminder_frequency", 2)
            _useDarkTheme.value = preferences.getBoolean("use_dark_theme", false)
        }
    }

    /**
     * Save settings to SharedPreferences
     */
    private fun saveSettings() {
        preferences.edit().apply {
            putBoolean("detect_female_faces", _detectFemaleFaces.value)
            putBoolean("detect_nsfw", _detectNsfw.value)
            putBoolean("detect_nudity", _detectNudity.value)
            putBoolean("detect_suggestive", _detectSuggestive.value)
            putInt("sensitivity_level", _sensitivityLevel.value)
            putInt("blur_effect_type", _blurEffectType.value)
            putInt("reminder_frequency", _reminderFrequency.value)
            putBoolean("use_dark_theme", _useDarkTheme.value)
            apply()
        }
    }

    /**
     * Toggle female face detection
     */
    fun toggleDetectFemaleFaces(enabled: Boolean) {
        _detectFemaleFaces.value = enabled
        saveSettings()
    }

    /**
     * Toggle NSFW detection
     */
    fun toggleDetectNsfw(enabled: Boolean) {
        _detectNsfw.value = enabled
        saveSettings()
    }

    /**
     * Toggle nudity detection
     */
    fun toggleDetectNudity(enabled: Boolean) {
        _detectNudity.value = enabled
        saveSettings()
    }

    /**
     * Toggle suggestive content detection
     */
    fun toggleDetectSuggestive(enabled: Boolean) {
        _detectSuggestive.value = enabled
        saveSettings()
    }

    /**
     * Set sensitivity level
     */
    fun setSensitivityLevel(level: Int) {
        _sensitivityLevel.value = level
        saveSettings()
    }

    /**
     * Set blur effect type
     */
    fun setBlurEffectType(type: Int) {
        _blurEffectType.value = type
        saveSettings()
    }

    /**
     * Set reminder frequency
     */
    fun setReminderFrequency(frequency: Int) {
        _reminderFrequency.value = frequency
        saveSettings()
    }

    /**
     * Toggle dark theme
     */
    fun toggleDarkTheme(useDark: Boolean) {
        _useDarkTheme.value = useDark
        saveSettings()
    }

    /**
     * Reset all settings to defaults
     */
    fun resetToDefaults() {
        _detectFemaleFaces.value = true
        _detectNsfw.value = true
        _detectNudity.value = true
        _detectSuggestive.value = false
        _sensitivityLevel.value = 50
        _blurEffectType.value = 1
        _reminderFrequency.value = 2
        _useDarkTheme.value = false
        saveSettings()
    }
}
