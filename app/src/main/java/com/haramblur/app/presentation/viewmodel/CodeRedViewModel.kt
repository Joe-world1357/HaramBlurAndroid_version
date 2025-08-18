package com.haramblur.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haramblur.app.domain.model.CodeRedSession
import com.haramblur.app.domain.model.LockableApp
import com.haramblur.app.domain.repository.CodeRedRepository
import com.haramblur.app.domain.usecase.codered.CreateCodeRedSessionUseCase
import com.haramblur.app.domain.usecase.codered.ManageCodeRedSessionUseCase
import com.haramblur.app.domain.usecase.codered.PasswordStrength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for CODE RED screen
 */
@HiltViewModel
class CodeRedViewModel @Inject constructor(
    private val codeRedRepository: CodeRedRepository,
    private val createCodeRedSessionUseCase: CreateCodeRedSessionUseCase,
    private val manageCodeRedSessionUseCase: ManageCodeRedSessionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CodeRedUiState())
    val uiState: StateFlow<CodeRedUiState> = _uiState.asStateFlow()
    
    // Active session flow
    val activeSession: StateFlow<CodeRedSession?> = manageCodeRedSessionUseCase.getActiveSession()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    init {
        loadLockableApps()
    }
    
    /**
     * Load all lockable apps
     */
    private fun loadLockableApps() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val apps = codeRedRepository.getLockableApps()
                _uiState.value = _uiState.value.copy(
                    lockableApps = apps,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load apps: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Toggle app selection for locking
     */
    fun toggleAppSelection(app: LockableApp) {
        val currentSelected = _uiState.value.selectedApps.toMutableSet()
        if (app in currentSelected) {
            currentSelected.remove(app)
        } else {
            currentSelected.add(app)
        }
        _uiState.value = _uiState.value.copy(selectedApps = currentSelected)
    }
    
    /**
     * Select all apps of a specific category
     */
    fun selectAppsByCategory(category: com.haramblur.app.domain.model.AppCategory) {
        val appsInCategory = _uiState.value.lockableApps.filter { it.category == category }
        val currentSelected = _uiState.value.selectedApps.toMutableSet()
        currentSelected.addAll(appsInCategory)
        _uiState.value = _uiState.value.copy(selectedApps = currentSelected)
    }
    
    /**
     * Clear all selected apps
     */
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedApps = emptySet())
    }
    
    /**
     * Update session duration
     */
    fun updateDuration(minutes: Int) {
        _uiState.value = _uiState.value.copy(durationMinutes = minutes)
    }
    
    /**
     * Update password
     */
    fun updatePassword(password: String) {
        val strength = createCodeRedSessionUseCase.validatePasswordStrength(password)
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordStrength = strength
        )
    }
    
    /**
     * Update reason
     */
    fun updateReason(reason: String) {
        _uiState.value = _uiState.value.copy(reason = reason)
    }
    
    /**
     * Create CODE RED session
     */
    fun createSession() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isCreatingSession = true)
                
                val selectedPackageNames = _uiState.value.selectedApps.map { it.packageName }
                val result = createCodeRedSessionUseCase.execute(
                    lockedApps = selectedPackageNames,
                    durationMinutes = _uiState.value.durationMinutes,
                    password = _uiState.value.password,
                    reason = _uiState.value.reason.takeIf { it.isNotBlank() }
                )
                
                result.fold(
                    onSuccess = { session ->
                        _uiState.value = _uiState.value.copy(
                            isCreatingSession = false,
                            sessionCreated = true,
                            error = null
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isCreatingSession = false,
                            error = error.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCreatingSession = false,
                    error = "Failed to create session: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Unlock session with password
     */
    fun unlockSession(password: String) {
        viewModelScope.launch {
            try {
                val session = activeSession.value
                if (session != null) {
                    val result = manageCodeRedSessionUseCase.unlockSession(session.sessionId, password)
                    when (result) {
                        is com.haramblur.app.domain.usecase.codered.UnlockResult.Success -> {
                            _uiState.value = _uiState.value.copy(
                                unlockError = null,
                                successMessage = "Session unlocked successfully"
                            )
                        }
                        is com.haramblur.app.domain.usecase.codered.UnlockResult.WrongPassword -> {
                            _uiState.value = _uiState.value.copy(
                                unlockError = "Incorrect password"
                            )
                        }
                        is com.haramblur.app.domain.usecase.codered.UnlockResult.SessionExpired -> {
                            _uiState.value = _uiState.value.copy(
                                successMessage = "Session has expired"
                            )
                        }
                        else -> {
                            _uiState.value = _uiState.value.copy(
                                unlockError = "Failed to unlock session"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    unlockError = "Error: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Get remaining time for active session
     */
    fun getRemainingTime(): Long {
        return viewModelScope.async {
            manageCodeRedSessionUseCase.getRemainingTime()
        }.let { deferred ->
            // Return 0 for now, in real implementation this would be handled properly
            0L
        }
    }
    
    /**
     * Clear error messages
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, unlockError = null)
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
    
    /**
     * Reset form after session creation
     */
    fun resetForm() {
        _uiState.value = CodeRedUiState()
        loadLockableApps()
    }
}

/**
 * UI state for CODE RED screen
 */
data class CodeRedUiState(
    val isLoading: Boolean = false,
    val isCreatingSession: Boolean = false,
    val lockableApps: List<LockableApp> = emptyList(),
    val selectedApps: Set<LockableApp> = emptySet(),
    val durationMinutes: Int = 60,
    val password: String = "",
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    val reason: String = "",
    val sessionCreated: Boolean = false,
    val error: String? = null,
    val unlockError: String? = null,
    val successMessage: String? = null
)