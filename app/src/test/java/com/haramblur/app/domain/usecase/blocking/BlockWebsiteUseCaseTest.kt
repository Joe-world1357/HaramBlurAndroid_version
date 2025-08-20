package com.haramblur.app.domain.usecase.blocking

import com.haramblur.app.domain.model.AppSettings
import com.haramblur.app.domain.model.BlockedSite
import com.haramblur.app.domain.model.ReminderCategory
import com.haramblur.app.domain.model.SiteCategory
import com.haramblur.app.domain.repository.BlockedSiteRepository
import com.haramblur.app.domain.repository.IslamicReminderRepository
import com.haramblur.app.domain.repository.SettingsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for BlockWebsiteUseCase
 */
class BlockWebsiteUseCaseTest {
    
    private val blockedSiteRepository = mockk<BlockedSiteRepository>()
    private val islamicReminderRepository = mockk<IslamicReminderRepository>()
    private val settingsRepository = mockk<SettingsRepository>()
    
    private lateinit var useCase: BlockWebsiteUseCase
    
    @Before
    fun setup() {
        useCase = BlockWebsiteUseCase(
            blockedSiteRepository,
            islamicReminderRepository,
            settingsRepository
        )
    }
    
    @Test
    fun `execute returns NotBlocked when website blocking is disabled`() = runTest {
        // Given
        val settings = AppSettings(enableWebsiteBlocking = false)
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        
        // When
        val result = useCase.execute("https://example.com")
        
        // Then
        assertEquals(BlockingResult.NotBlocked, result)
    }
    
    @Test
    fun `execute returns NotBlocked for invalid URL`() = runTest {
        // Given
        val settings = AppSettings(enableWebsiteBlocking = true)
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        
        // When
        val result = useCase.execute("invalid-url")
        
        // Then
        assertEquals(BlockingResult.NotBlocked, result)
    }
    
    @Test
    fun `execute returns Blocked for already blocked domain`() = runTest {
        // Given
        val settings = AppSettings(enableWebsiteBlocking = true)
        val url = "https://example.com/path"
        val domain = "example.com"
        
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        coEvery { blockedSiteRepository.isDomainBlocked(domain) } returns true
        coEvery { blockedSiteRepository.incrementBlockCount(domain) } just Runs
        coEvery { islamicReminderRepository.getContextualReminder(ReminderCategory.LOWERING_GAZE) } returns null
        
        // When
        val result = useCase.execute(url)
        
        // Then
        assertTrue(result is BlockingResult.Blocked)
        assertEquals(domain, (result as BlockingResult.Blocked).domain)
        coVerify { blockedSiteRepository.incrementBlockCount(domain) }
    }
    
    @Test
    fun `execute blocks adult content domains`() = runTest {
        // Given
        val settings = AppSettings(enableWebsiteBlocking = true)
        val url = "https://pornhub.com"
        val domain = "pornhub.com"
        
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        coEvery { blockedSiteRepository.isDomainBlocked(domain) } returns false
        coEvery { blockedSiteRepository.addBlockedSite(any()) } just Runs
        coEvery { islamicReminderRepository.getContextualReminder(ReminderCategory.LOWERING_GAZE) } returns null
        
        // When
        val result = useCase.execute(url)
        
        // Then
        assertTrue(result is BlockingResult.Blocked)
        assertEquals(domain, (result as BlockingResult.Blocked).domain)
        coVerify { blockedSiteRepository.addBlockedSite(any()) }
    }
    
    @Test
    fun `execute does not block safe domains`() = runTest {
        // Given
        val settings = AppSettings(enableWebsiteBlocking = true)
        val url = "https://google.com"
        val domain = "google.com"
        
        coEvery { settingsRepository.getCurrentSettings() } returns settings
        coEvery { blockedSiteRepository.isDomainBlocked(domain) } returns false
        
        // When
        val result = useCase.execute(url)
        
        // Then
        assertEquals(BlockingResult.NotBlocked, result)
        coVerify(exactly = 0) { blockedSiteRepository.addBlockedSite(any()) }
    }
}