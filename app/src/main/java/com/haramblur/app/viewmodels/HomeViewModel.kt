package com.haramblur.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.data.models.IslamicReminder
import com.haramblur.app.data.models.ProtectionStats
import com.haramblur.app.data.models.ProtectionStatus
import com.haramblur.app.data.repositories.DetectionRepository
import com.haramblur.app.data.repositories.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen
 */
class HomeViewModel(
    private val detectionRepository: DetectionRepository,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    // Protection status
    private val _protectionStatus = MutableStateFlow(ProtectionStatus.ACTIVE)
    val protectionStatus: StateFlow<ProtectionStatus> = _protectionStatus.asStateFlow()

    // Protection stats
    private val _protectionStats = MutableStateFlow(ProtectionStats())
    val protectionStats: StateFlow<ProtectionStats> = _protectionStats.asStateFlow()

    // Islamic reminder
    private val _currentReminder = MutableStateFlow<IslamicReminder?>(null)
    val currentReminder: StateFlow<IslamicReminder?> = _currentReminder.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProtectionStats()
        loadRandomReminder()
    }

    /**
     * Toggle protection status
     */
    fun toggleProtection(isActive: Boolean) {
        _protectionStatus.value = if (isActive) ProtectionStatus.ACTIVE else ProtectionStatus.INACTIVE
        // In a real app, this would trigger system-level changes
    }

    /**
     * Load protection statistics
     */
    fun loadProtectionStats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _protectionStats.value = detectionRepository.getTodayStats()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load a random Islamic reminder
     */
    fun loadRandomReminder() {
        viewModelScope.launch {
            try {
                _currentReminder.value = reminderRepository.getRandomReminder()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
