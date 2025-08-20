package com.haramblur.app.domain.usecase.blocking

import com.haramblur.app.domain.model.BlockedSite
import com.haramblur.app.domain.model.SiteCategory
import com.haramblur.app.domain.repository.BlockedSiteRepository
import com.haramblur.app.domain.repository.IslamicReminderRepository
import com.haramblur.app.domain.repository.SettingsRepository
import java.net.URL
import javax.inject.Inject

/**
 * Use case for blocking inappropriate websites
 */
class BlockWebsiteUseCase @Inject constructor(
    private val blockedSiteRepository: BlockedSiteRepository,
    private val islamicReminderRepository: IslamicReminderRepository,
    private val settingsRepository: SettingsRepository
) {
    
    /**
     * Check if a URL should be blocked and handle the blocking
     */
    suspend fun execute(url: String): BlockingResult {
        val settings = settingsRepository.getCurrentSettings()
        
        if (!settings.enableWebsiteBlocking) {
            return BlockingResult.NotBlocked
        }
        
        val domain = extractDomain(url)
        if (domain == null) {
            return BlockingResult.NotBlocked
        }
        
        // Check if domain is in blocked list
        val isBlocked = blockedSiteRepository.isDomainBlocked(domain)
        
        if (isBlocked) {
            // Increment block count
            blockedSiteRepository.incrementBlockCount(domain)
            
            // Get appropriate Islamic reminder
            val reminder = islamicReminderRepository.getContextualReminder(
                category = com.haramblur.app.domain.model.ReminderCategory.LOWERING_GAZE
            )
            
            return BlockingResult.Blocked(
                domain = domain,
                reminder = reminder,
                blockTime = System.currentTimeMillis()
            )
        }
        
        // Check against predefined patterns
        val category = categorizeUrl(url)
        if (shouldBlockCategory(category)) {
            // Add to blocked sites
            val blockedSite = BlockedSite(
                domain = domain,
                category = category,
                firstBlocked = System.currentTimeMillis(),
                userAdded = false
            )
            blockedSiteRepository.addBlockedSite(blockedSite)
            
            // Get appropriate reminder
            val reminder = islamicReminderRepository.getContextualReminder(
                category = com.haramblur.app.domain.model.ReminderCategory.LOWERING_GAZE
            )
            
            return BlockingResult.Blocked(
                domain = domain,
                reminder = reminder,
                blockTime = System.currentTimeMillis()
            )
        }
        
        return BlockingResult.NotBlocked
    }
    
    /**
     * Extract domain from URL
     */
    private fun extractDomain(url: String): String? {
        return try {
            val cleanUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "http://$url"
            } else {
                url
            }
            URL(cleanUrl).host.lowercase()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Categorize URL based on domain patterns
     */
    private fun categorizeUrl(url: String): SiteCategory {
        val domain = extractDomain(url)?.lowercase() ?: return SiteCategory.UNKNOWN
        
        return when {
            isAdultContent(domain) -> SiteCategory.ADULT_CONTENT
            isSocialMedia(domain) -> SiteCategory.SOCIAL_MEDIA
            isDating(domain) -> SiteCategory.DATING
            isGambling(domain) -> SiteCategory.GAMBLING
            isEntertainment(domain) -> SiteCategory.ENTERTAINMENT
            else -> SiteCategory.UNKNOWN
        }
    }
    
    /**
     * Check if domain contains adult content patterns
     */
    private fun isAdultContent(domain: String): Boolean {
        val adultPatterns = listOf(
            "porn", "xxx", "sex", "adult", "nude", "naked", "erotic",
            "xnxx", "xvideos", "pornhub", "redtube", "youporn"
        )
        return adultPatterns.any { domain.contains(it) }
    }
    
    /**
     * Check if domain is social media
     */
    private fun isSocialMedia(domain: String): Boolean {
        val socialMediaDomains = listOf(
            "facebook.com", "instagram.com", "twitter.com", "x.com",
            "tiktok.com", "snapchat.com", "linkedin.com", "pinterest.com"
        )
        return socialMediaDomains.any { domain.contains(it) }
    }
    
    /**
     * Check if domain is dating related
     */
    private fun isDating(domain: String): Boolean {
        val datingDomains = listOf(
            "tinder.com", "bumble.com", "match.com", "okcupid.com",
            "pof.com", "eharmony.com", "zoosk.com", "badoo.com"
        )
        return datingDomains.any { domain.contains(it) }
    }
    
    /**
     * Check if domain is gambling related
     */
    private fun isGambling(domain: String): Boolean {
        val gamblingPatterns = listOf(
            "casino", "poker", "bet", "gambling", "lottery", "slots"
        )
        return gamblingPatterns.any { domain.contains(it) }
    }
    
    /**
     * Check if domain is entertainment that might be distracting
     */
    private fun isEntertainment(domain: String): Boolean {
        val entertainmentDomains = listOf(
            "youtube.com", "netflix.com", "twitch.tv", "reddit.com"
        )
        return entertainmentDomains.any { domain.contains(it) }
    }
    
    /**
     * Check if a category should be blocked based on settings
     */
    private fun shouldBlockCategory(category: SiteCategory): Boolean {
        return when (category) {
            SiteCategory.ADULT_CONTENT -> true
            SiteCategory.DATING -> true
            SiteCategory.GAMBLING -> true
            else -> false
        }
    }
}

/**
 * Result of website blocking check
 */
sealed class BlockingResult {
    object NotBlocked : BlockingResult()
    
    data class Blocked(
        val domain: String,
        val reminder: com.haramblur.app.domain.model.IslamicReminder?,
        val blockTime: Long
    ) : BlockingResult()
}