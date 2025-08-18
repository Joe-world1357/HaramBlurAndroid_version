package com.haramblur.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.domain.model.AppSettings
import com.haramblur.app.domain.repository.DetectionRepository
import com.haramblur.app.domain.repository.DetectionStats
import com.haramblur.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val detectionRepository: DetectionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Observe settings
    val settings: StateFlow<AppSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )
    
    init {
        loadDetectionStats()
    }
    
    /**
     * Toggle protection on/off
     */
    fun toggleProtection() {
        viewModelScope.launch {
            val currentSettings = settings.value
            settingsRepository.updateProtectionEnabled(!currentSettings.protectionEnabled)
        }
    }
    
    /**
     * Load detection statistics
     */
    private fun loadDetectionStats() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val stats = detectionRepository.getDetectionStats()
                _uiState.value = _uiState.value.copy(
                    detectionStats = stats,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    /**
     * Refresh statistics
     */
    fun refreshStats() {
        loadDetectionStats()
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * UI state for Home screen
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val detectionStats: DetectionStats = DetectionStats(),
    val error: String? = null
)