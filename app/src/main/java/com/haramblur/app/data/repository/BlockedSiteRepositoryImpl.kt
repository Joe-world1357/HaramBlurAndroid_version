package com.haramblur.app.data.repository

import com.haramblur.app.data.local.dao.BlockedSiteDao
import com.haramblur.app.data.local.entity.toDomain
import com.haramblur.app.data.local.entity.toEntity
import com.haramblur.app.domain.model.BlockedSite
import com.haramblur.app.domain.model.SiteCategory
import com.haramblur.app.domain.repository.BlockedSiteRepository
import com.haramblur.app.domain.repository.BlockingStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of BlockedSiteRepository
 */
@Singleton
class BlockedSiteRepositoryImpl @Inject constructor(
    private val blockedSiteDao: BlockedSiteDao
) : BlockedSiteRepository {
    
    override suspend fun addBlockedSite(site: BlockedSite) {
        blockedSiteDao.insertBlockedSite(site.toEntity())
    }
    
    override suspend fun updateBlockedSite(site: BlockedSite) {
        blockedSiteDao.updateBlockedSite(site.toEntity())
    }
    
    override suspend fun removeBlockedSite(domain: String) {
        blockedSiteDao.deleteBlockedSiteByDomain(domain)
    }
    
    override fun getAllBlockedSites(): Flow<List<BlockedSite>> {
        return blockedSiteDao.getAllActiveBlockedSites().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getBlockedSitesByCategory(category: SiteCategory): Flow<List<BlockedSite>> {
        return blockedSiteDao.getBlockedSitesByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun isDomainBlocked(domain: String): Boolean {
        return blockedSiteDao.isDomainBlocked(domain)
    }
    
    override suspend fun getBlockedSite(domain: String): BlockedSite? {
        return blockedSiteDao.getBlockedSiteByDomain(domain)?.toDomain()
    }
    
    override suspend fun incrementBlockCount(domain: String) {
        blockedSiteDao.incrementBlockCount(domain, System.currentTimeMillis())
    }
    
    override suspend fun getBlockingStats(): BlockingStats {
        val totalBlockedSites = blockedSiteDao.getTotalBlockedSitesCount()
        val totalBlocks = blockedSiteDao.getTotalBlocksCount() ?: 0
        
        // Calculate today's range
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        
        val todayBlocks = blockedSiteDao.getTodayBlocksCount(startOfDay, endOfDay) ?: 0
        
        // Calculate week range
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startOfWeek = calendar.timeInMillis
        val weekBlocks = blockedSiteDao.getWeekBlocksCount(startOfWeek) ?: 0
        
        // Calculate month range
        calendar.add(Calendar.DAY_OF_YEAR, -23) // Total 30 days
        val startOfMonth = calendar.timeInMillis
        val monthBlocks = blockedSiteDao.getMonthBlocksCount(startOfMonth) ?: 0
        
        val mostBlockedDomainResult = blockedSiteDao.getMostBlockedDomain()
        val mostBlockedCategoryResult = blockedSiteDao.getMostBlockedCategory()
        val mostBlockedCategory = mostBlockedCategoryResult?.let {
            try {
                SiteCategory.valueOf(it.category)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        
        val lastBlockTime = blockedSiteDao.getLastBlockTime()
        
        return BlockingStats(
            totalBlockedSites = totalBlockedSites,
            totalBlocks = totalBlocks,
            todayBlocks = todayBlocks,
            weekBlocks = weekBlocks,
            monthBlocks = monthBlocks,
            mostBlockedDomain = mostBlockedDomainResult?.domain,
            mostBlockedCategory = mostBlockedCategory,
            lastBlockTime = lastBlockTime
        )
    }
    
    override suspend fun clearAllBlockedSites() {
        blockedSiteDao.clearAllBlockedSites()
    }
}