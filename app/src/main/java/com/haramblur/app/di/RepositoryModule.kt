package com.haramblur.app.di

import com.haramblur.app.data.repository.*
import com.haramblur.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindDetectionRepository(
        detectionRepositoryImpl: DetectionRepositoryImpl
    ): DetectionRepository
    
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
    
    // TODO: Add other repository bindings as they are implemented
    // @Binds
    // @Singleton
    // abstract fun bindBlockedSiteRepository(
    //     blockedSiteRepositoryImpl: BlockedSiteRepositoryImpl
    // ): BlockedSiteRepository
    
    // @Binds
    // @Singleton
    // abstract fun bindIslamicReminderRepository(
    //     islamicReminderRepositoryImpl: IslamicReminderRepositoryImpl
    // ): IslamicReminderRepository
    
    // @Binds
    // @Singleton
    // abstract fun bindCodeRedRepository(
    //     codeRedRepositoryImpl: CodeRedRepositoryImpl
    // ): CodeRedRepository
}