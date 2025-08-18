package com.haramblur.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haramblur.app.domain.model.BlockedSite
import com.haramblur.app.domain.model.SiteCategory

/**
 * Room entity for blocked sites
 */
@Entity(tableName = "blocked_sites")
data class BlockedSiteEntity(
    @PrimaryKey
    val domain: String,
    val category: String,
    val firstBlocked: Long,
    val blockCount: Int,
    val lastBlocked: Long,
    val isActive: Boolean,
    val userAdded: Boolean,
    val reason: String?
)

/**
 * Convert domain model to entity
 */
fun BlockedSite.toEntity(): BlockedSiteEntity {
    return BlockedSiteEntity(
        domain = domain,
        category = category.name,
        firstBlocked = firstBlocked,
        blockCount = blockCount,
        lastBlocked = lastBlocked,
        isActive = isActive,
        userAdded = userAdded,
        reason = reason
    )
}

/**
 * Convert entity to domain model
 */
fun BlockedSiteEntity.toDomain(): BlockedSite {
    return BlockedSite(
        domain = domain,
        category = SiteCategory.valueOf(category),
        firstBlocked = firstBlocked,
        blockCount = blockCount,
        lastBlocked = lastBlocked,
        isActive = isActive,
        userAdded = userAdded,
        reason = reason
    )
}