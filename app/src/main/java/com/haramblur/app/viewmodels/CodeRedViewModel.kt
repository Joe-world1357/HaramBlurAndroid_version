package com.haramblur.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.data.models.AppInfo
import com.haramblur.app.data.models.LockPreset
import com.haramblur.app.data.models.LockSession
import com.haramblur.app.data.repositories.CodeRedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the CODE RED screen
 */
class CodeRedViewModel(private val codeRedRepository: CodeRedRepository) : ViewModel() {

    // Selected apps for locking
    private val _selectedApps = MutableStateFlow<List<String>>(emptyList())
    val selectedApps: StateFlow<List<String>> = _selectedApps.asStateFlow()

    // Duration in minutes
    private val _durationMinutes = MutableStateFlow(60) // Default 1 hour
    val durationMinutes: StateFlow<Int> = _durationMinutes.asStateFlow()

    // Reason for lock
    private val _lockReason = MutableStateFlow("")
    val lockReason: StateFlow<String> = _lockReason.asStateFlow()

    // Available presets
    private val _lockPresets = MutableStateFlow<List<LockPreset>>(emptyList())
    val lockPresets: StateFlow<List<LockPreset>> = _lockPresets.asStateFlow()

    // Active sessions
    private val _activeSessions = MutableStateFlow<List<LockSession>>(emptyList())
    val activeSessions: StateFlow<List<LockSession>> = _activeSessions.asStateFlow()

    // Available apps
    private val _availableApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val availableApps: StateFlow<List<AppInfo>> = _availableApps.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Success state
    private val _lockActivated = MutableStateFlow(false)
    val lockActivated: StateFlow<Boolean> = _lockActivated.asStateFlow()

    init {
        loadPresets()
        loadActiveSessions()
        loadAvailableApps()
    }

    /**
     * Load available lock presets
     */
    fun loadPresets() {
        viewModelScope.launch {
            try {
                codeRedRepository.getLockPresets().collect { presets ->
                    _lockPresets.value = presets
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Load active lock sessions
     */
    fun loadActiveSessions() {
        viewModelScope.launch {
            try {
                codeRedRepository.getActiveSessions().collect { sessions ->
                    _activeSessions.value = sessions
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Load available apps
     */
    fun loadAvailableApps() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apps = codeRedRepository.getInstalledApps()
                
                // Check which apps are already locked
                val activeSessions = codeRedRepository.getActiveSessions().first()
                val lockedPackages = activeSessions.flatMap { it.lockedApps }.toSet()
                
                // Update locked status
                _availableApps.value = apps.map { app ->
                    app.copy(isLocked = lockedPackages.contains(app.packageName))
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggle app selection
     */
    fun toggleAppSelection(packageName: String) {
        val currentList = _selectedApps.value.toMutableList()
        if (currentList.contains(packageName)) {
            currentList.remove(packageName)
        } else {
            currentList.add(packageName)
        }
        _selectedApps.value = currentList
    }

    /**
     * Set lock duration
     */
    fun setDuration(minutes: Int) {
        _durationMinutes.value = minutes
    }

    /**
     * Set lock reason
     */
    fun setReason(reason: String) {
        _lockReason.value = reason
    }

    /**
     * Apply a preset
     */
    fun applyPreset(presetId: Long) {
        viewModelScope.launch {
            val preset = _lockPresets.value.find { it.id == presetId }
            preset?.let {
                _selectedApps.value = it.apps
                _durationMinutes.value = it.durationMinutes
            }
        }
    }

    /**
     * Activate lock
     */
    fun activateLock() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sessionId = codeRedRepository.startLockSession(
                    apps = _selectedApps.value,
                    durationMinutes = _durationMinutes.value,
                    reason = _lockReason.value
                )
                
                // Reset state
                _selectedApps.value = emptyList()
                _lockReason.value = ""
                _lockActivated.value = true
                
                // Refresh active sessions
                loadActiveSessions()
                loadAvailableApps()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * End a lock session
     */
    fun endLockSession(sessionId: Long) {
        viewModelScope.launch {
            try {
                codeRedRepository.endLockSession(sessionId)
                loadActiveSessions()
                loadAvailableApps()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * End all active lock sessions
     */
    fun endAllLockSessions() {
        viewModelScope.launch {
            try {
                codeRedRepository.endAllLockSessions()
                loadActiveSessions()
                loadAvailableApps()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Create a new preset from current selection
     */
    fun saveAsPreset(name: String) {
        viewModelScope.launch {
            try {
                codeRedRepository.createLockPreset(
                    name = name,
                    apps = _selectedApps.value,
                    durationMinutes = _durationMinutes.value
                )
                loadPresets()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Reset lock activated state
     */
    fun resetLockActivatedState() {
        _lockActivated.value = false
    }
}
