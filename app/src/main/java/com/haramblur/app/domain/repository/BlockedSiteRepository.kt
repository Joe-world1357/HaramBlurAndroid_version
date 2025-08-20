package com.haramblur.app.domain.repository

import com.haramblur.app.domain.model.BlockedSite
import com.haramblur.app.domain.model.SiteCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for blocked site operations
 */
interface BlockedSiteRepository {
    
    /**
     * Add a new blocked site
     */
    suspend fun addBlockedSite(site: BlockedSite)
    
    /**
     * Update an existing blocked site
     */
    suspend fun updateBlockedSite(site: BlockedSite)
    
    /**
     * Remove a blocked site
     */
    suspend fun removeBlockedSite(domain: String)
    
    /**
     * Get all blocked sites
     */
    fun getAllBlockedSites(): Flow<List<BlockedSite>>
    
    /**
     * Get blocked sites by category
     */
    fun getBlockedSitesByCategory(category: SiteCategory): Flow<List<BlockedSite>>
    
    /**
     * Check if a domain is blocked
     */
    suspend fun isDomainBlocked(domain: String): Boolean
    
    /**
     * Get blocked site by domain
     */
    suspend fun getBlockedSite(domain: String): BlockedSite?
    
    /**
     * Increment block count for a domain
     */
    suspend fun incrementBlockCount(domain: String)
    
    /**
     * Get blocking statistics
     */
    suspend fun getBlockingStats(): BlockingStats
    
    /**
     * Clear all blocked sites
     */
    suspend fun clearAllBlockedSites()
}

/**
 * Statistics for website blocking
 */
data class BlockingStats(
    val totalBlockedSites: Int = 0,
    val totalBlocks: Int = 0,
    val todayBlocks: Int = 0,
    val weekBlocks: Int = 0,
    val monthBlocks: Int = 0,
    val mostBlockedDomain: String? = null,
    val mostBlockedCategory: SiteCategory? = null,
    val lastBlockTime: Long? = null
)