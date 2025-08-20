package com.haramblur.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.domain.model.*
import com.haramblur.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Settings screen
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    // Observe settings
    val settings: StateFlow<AppSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )
    
    /**
     * Update detection sensitivity
     */
    fun updateDetectionSensitivity(sensitivity: Float) {
        viewModelScope.launch {
            try {
                settingsRepository.updateDetectionSensitivity(sensitivity)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update sensitivity: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Update blur type
     */
    fun updateBlurType(blurType: BlurType) {
        viewModelScope.launch {
            try {
                settingsRepository.updateBlurType(blurType)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update blur type: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Update blur intensity
     */
    fun updateBlurIntensity(intensity: BlurIntensity) {
        viewModelScope.launch {
            try {
                settingsRepository.updateBlurIntensity(intensity)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update blur intensity: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Toggle website blocking
     */
    fun toggleWebsiteBlocking() {
        viewModelScope.launch {
            try {
                val currentSettings = settings.value
                settingsRepository.updateWebsiteBlocking(!currentSettings.enableWebsiteBlocking)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to toggle website blocking: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Toggle Islamic reminders
     */
    fun toggleIslamicReminders() {
        viewModelScope.launch {
            try {
                val currentSettings = settings.value
                settingsRepository.updateIslamicReminders(!currentSettings.enableIslamicReminders)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to toggle Islamic reminders: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Update reminder frequency
     */
    fun updateReminderFrequency(frequency: ReminderFrequency) {
        viewModelScope.launch {
            try {
                settingsRepository.updateReminderFrequency(frequency)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update reminder frequency: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Update performance mode
     */
    fun updatePerformanceMode(mode: PerformanceMode) {
        viewModelScope.launch {
            try {
                settingsRepository.updatePerformanceMode(mode)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update performance mode: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Update theme mode
     */
    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            try {
                settingsRepository.updateThemeMode(mode)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update theme mode: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Reset settings to defaults
     */
    fun resetToDefaults() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                settingsRepository.resetToDefaults()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Settings reset to defaults"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to reset settings: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Export settings
     */
    fun exportSettings(): String? {
        return try {
            // This would normally be done in a coroutine, but for simplicity
            // we'll make it synchronous for now
            // In a real implementation, this should be properly handled
            null // TODO: Implement proper export functionality
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to export settings: ${e.message}"
            )
            null
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}

/**
 * UI state for Settings screen
 */
data class SettingsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)