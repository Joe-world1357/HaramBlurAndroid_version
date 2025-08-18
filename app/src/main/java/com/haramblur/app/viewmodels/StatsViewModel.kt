package com.haramblur.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.data.models.DetectionEvent
import com.haramblur.app.data.models.ProtectionStats
import com.haramblur.app.data.repositories.DetectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Statistics screen
 */
class StatsViewModel(private val detectionRepository: DetectionRepository) : ViewModel() {

    // Time period selection (0: Today, 1: Week, 2: Month)
    private val _selectedTimePeriod = MutableStateFlow(0)
    val selectedTimePeriod: StateFlow<Int> = _selectedTimePeriod.asStateFlow()

    // Detection events
    private val _detectionEvents = MutableStateFlow<List<DetectionEvent>>(emptyList())
    val detectionEvents: StateFlow<List<DetectionEvent>> = _detectionEvents.asStateFlow()

    // Protection stats
    private val _protectionStats = MutableStateFlow(ProtectionStats())
    val protectionStats: StateFlow<ProtectionStats> = _protectionStats.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTodayData()
    }

    /**
     * Set the selected time period
     */
    fun setTimePeriod(periodIndex: Int) {
        if (_selectedTimePeriod.value != periodIndex) {
            _selectedTimePeriod.value = periodIndex
            
            when (periodIndex) {
                0 -> loadTodayData()
                1 -> loadWeekData()
                2 -> loadMonthData()
            }
        }
    }

    /**
     * Load today's data
     */
    private fun loadTodayData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load protection stats
                _protectionStats.value = detectionRepository.getTodayStats()
                
                // Load detection events
                detectionRepository.getTodayEvents().collect { events ->
                    _detectionEvents.value = events
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                // Handle error
                _isLoading.value = false
            }
        }
    }

    /**
     * Load week data
     */
    private fun loadWeekData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // For now, just load the events - in a real app we'd calculate weekly stats
                detectionRepository.getWeekEvents().collect { events ->
                    _detectionEvents.value = events
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                // Handle error
                _isLoading.value = false
            }
        }
    }

    /**
     * Load month data
     */
    private fun loadMonthData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // For now, just load the events - in a real app we'd calculate monthly stats
                detectionRepository.getMonthEvents().collect { events ->
                    _detectionEvents.value = events
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                // Handle error
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear detection history
     */
    fun clearHistory() {
        viewModelScope.launch {
            try {
                detectionRepository.clearHistory()
                
                // Reload data based on current time period
                when (_selectedTimePeriod.value) {
                    0 -> loadTodayData()
                    1 -> loadWeekData()
                    2 -> loadMonthData()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Get daily detection counts for the selected period
     */
    fun getDailyDetectionCounts(): Map<String, Int> {
        // This would be implemented to provide data for charts
        // For now, return dummy data
        return mapOf(
            "Mon" to 4,
            "Tue" to 8,
            "Wed" to 6,
            "Thu" to 3,
            "Fri" to 6,
            "Sat" to 4,
            "Sun" to 10
        )
    }
}
