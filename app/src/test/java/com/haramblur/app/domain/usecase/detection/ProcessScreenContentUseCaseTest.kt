package com.haramblur.app.domain.usecase.detection

import android.graphics.Bitmap
import com.haramblur.app.domain.model.AppSettings
import com.haramblur.app.domain.repository.DetectionRepository
import com.haramblur.app.domain.repository.SettingsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ProcessScreenContentUseCase
 */
class ProcessScreenContentUseCaseTest {
    
    private val detectionRepository = mockk<DetectionRepository>()
    private val settingsRepository = mockk<SettingsRepository>()
    private val bitmap = mockk<Bitmap>()
    
    private lateinit var useCase: ProcessScreenContentUseCase
    
    @Before
    fun setup() {
        useCase = ProcessScreenContentUseCase(detectionRepository, settingsRepository)
        
        // Mock bitmap properties
        every { bitmap.width } returns 720
        every { bitmap.height } returns 1280
        every { bitmap.isRecycled } returns false
    }
    
    @Test
    fun `execute returns empty list when protection is disabled`() = runTest {
        // Given
        val settings = AppSettings(protectionEnabled = false)
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        
        // When
        val result = useCase.execute(bitmap)
        
        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 0) { detectionRepository.insertDetection(any()) }
    }
    
    @Test
    fun `execute processes bitmap when protection is enabled`() = runTest {
        // Given
        val settings = AppSettings(protectionEnabled = true)
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        coEvery { detectionRepository.insertDetection(any()) } just Runs
        
        // When
        val result = useCase.execute(bitmap)
        
        // Then
        // For now, the use case returns empty list as AI detection is not implemented
        assertTrue(result.isEmpty())
        coVerify { settingsRepository.getCurrentSettings() }
    }
    
    @Test
    fun `execute handles exceptions gracefully`() = runTest {
        // Given
        val settings = AppSettings(protectionEnabled = true)
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        coEvery { detectionRepository.insertDetection(any()) } throws RuntimeException("Database error")
        
        // When
        val result = useCase.execute(bitmap)
        
        // Then
        // Should not throw exception and return empty list
        assertTrue(result.isEmpty())
    }
}