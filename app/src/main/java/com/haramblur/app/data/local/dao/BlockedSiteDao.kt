package com.haramblur.app.data.local.dao

import androidx.room.*
import com.haramblur.app.data.local.entity.BlockedSiteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for blocked sites
 */
@Dao
interface BlockedSiteDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedSite(site: BlockedSiteEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedSites(sites: List<BlockedSiteEntity>)
    
    @Update
    suspend fun updateBlockedSite(site: BlockedSiteEntity)
    
    @Delete
    suspend fun deleteBlockedSite(site: BlockedSiteEntity)
    
    @Query("DELETE FROM blocked_sites WHERE domain = :domain")
    suspend fun deleteBlockedSiteByDomain(domain: String)
    
    @Query("SELECT * FROM blocked_sites WHERE isActive = 1 ORDER BY domain ASC")
    fun getAllActiveBlockedSites(): Flow<List<BlockedSiteEntity>>
    
    @Query("SELECT * FROM blocked_sites ORDER BY domain ASC")
    fun getAllBlockedSites(): Flow<List<BlockedSiteEntity>>
    
    @Query("SELECT * FROM blocked_sites WHERE category = :category AND isActive = 1 ORDER BY domain ASC")
    fun getBlockedSitesByCategory(category: String): Flow<List<BlockedSiteEntity>>
    
    @Query("SELECT * FROM blocked_sites WHERE domain = :domain")
    suspend fun getBlockedSiteByDomain(domain: String): BlockedSiteEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM blocked_sites WHERE domain = :domain AND isActive = 1)")
    suspend fun isDomainBlocked(domain: String): Boolean
    
    @Query("UPDATE blocked_sites SET blockCount = blockCount + 1, lastBlocked = :currentTime WHERE domain = :domain")
    suspend fun incrementBlockCount(domain: String, currentTime: Long)
    
    @Query("SELECT COUNT(*) FROM blocked_sites WHERE isActive = 1")
    suspend fun getTotalBlockedSitesCount(): Int
    
    @Query("SELECT SUM(blockCount) FROM blocked_sites WHERE isActive = 1")
    suspend fun getTotalBlocksCount(): Int?
    
    @Query("""
        SELECT SUM(blockCount) FROM blocked_sites 
        WHERE isActive = 1 AND lastBlocked >= :startOfDay AND lastBlocked < :endOfDay
    """)
    suspend fun getTodayBlocksCount(startOfDay: Long, endOfDay: Long): Int?
    
    @Query("SELECT SUM(blockCount) FROM blocked_sites WHERE isActive = 1 AND lastBlocked >= :startOfWeek")
    suspend fun getWeekBlocksCount(startOfWeek: Long): Int?
    
    @Query("SELECT SUM(blockCount) FROM blocked_sites WHERE isActive = 1 AND lastBlocked >= :startOfMonth")
    suspend fun getMonthBlocksCount(startOfMonth: Long): Int?
    
    @Query("SELECT domain, blockCount FROM blocked_sites WHERE isActive = 1 ORDER BY blockCount DESC LIMIT 1")
    suspend fun getMostBlockedDomain(): DomainBlockCount?
    
    @Query("SELECT category, SUM(blockCount) as totalBlocks FROM blocked_sites WHERE isActive = 1 GROUP BY category ORDER BY totalBlocks DESC LIMIT 1")
    suspend fun getMostBlockedCategory(): CategoryBlockCount?
    
    @Query("SELECT MAX(lastBlocked) FROM blocked_sites WHERE isActive = 1")
    suspend fun getLastBlockTime(): Long?
    
    @Query("DELETE FROM blocked_sites")
    suspend fun clearAllBlockedSites()
    
    @Query("UPDATE blocked_sites SET isActive = 0 WHERE domain = :domain")
    suspend fun deactivateBlockedSite(domain: String)
    
    @Query("UPDATE blocked_sites SET isActive = 1 WHERE domain = :domain")
    suspend fun activateBlockedSite(domain: String)
}

/**
 * Data class for domain block count query result
 */
data class DomainBlockCount(
    val domain: String,
    val blockCount: Int
)

/**
 * Data class for category block count query result
 */
data class CategoryBlockCount(
    val category: String,
    val totalBlocks: Int
)