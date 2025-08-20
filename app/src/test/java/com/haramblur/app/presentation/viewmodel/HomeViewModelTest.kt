package com.haramblur.app.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.haramblur.app.domain.model.AppSettings
import com.haramblur.app.domain.repository.DetectionRepository
import com.haramblur.app.domain.repository.DetectionStats
import com.haramblur.app.domain.repository.SettingsRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for HomeViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = UnconfinedTestDispatcher()
    
    private val settingsRepository = mockk<SettingsRepository>()
    private val detectionRepository = mockk<DetectionRepository>()
    
    private lateinit var viewModel: HomeViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Setup default mocks
        every { settingsRepository.getSettings() } returns flowOf(AppSettings())
        coEvery { detectionRepository.getDetectionStats() } returns DetectionStats()
        
        viewModel = HomeViewModel(settingsRepository, detectionRepository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state should have default values`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)
            assertEquals(DetectionStats(), initialState.detectionStats)
            assertNull(initialState.error)
        }
    }
    
    @Test
    fun `settings flow should emit current settings`() = runTest {
        val testSettings = AppSettings(protectionEnabled = false, detectionSensitivity = 0.5f)
        every { settingsRepository.getSettings() } returns flowOf(testSettings)
        
        val newViewModel = HomeViewModel(settingsRepository, detectionRepository)
        
        newViewModel.settings.test {
            val settings = awaitItem()
            assertFalse(settings.protectionEnabled)
            assertEquals(0.5f, settings.detectionSensitivity, 0.01f)
        }
    }
    
    @Test
    fun `toggleProtection should update protection setting`() = runTest {
        val currentSettings = AppSettings(protectionEnabled = true)
        every { settingsRepository.getSettings() } returns flowOf(currentSettings)
        coEvery { settingsRepository.updateProtectionEnabled(any()) } just Runs
        
        viewModel.toggleProtection()
        
        coVerify { settingsRepository.updateProtectionEnabled(false) }
    }
    
    @Test
    fun `refreshStats should update detection statistics`() = runTest {
        val testStats = DetectionStats(totalDetections = 10, todayDetections = 5)
        coEvery { detectionRepository.getDetectionStats() } returns testStats
        
        viewModel.refreshStats()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(10, state.detectionStats.totalDetections)
            assertEquals(5, state.detectionStats.todayDetections)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }
    
    @Test
    fun `error during stats loading should update error state`() = runTest {
        val errorMessage = "Network error"
        coEvery { detectionRepository.getDetectionStats() } throws RuntimeException(errorMessage)
        
        viewModel.refreshStats()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(errorMessage, state.error)
        }
    }
    
    @Test
    fun `clearError should reset error state`() = runTest {
        // First set an error
        coEvery { detectionRepository.getDetectionStats() } throws RuntimeException("Test error")
        viewModel.refreshStats()
        
        // Then clear it
        viewModel.clearError()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }
}