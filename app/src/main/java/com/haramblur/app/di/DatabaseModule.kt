package com.haramblur.app.di

import android.content.Context
import androidx.room.Room
import com.haramblur.app.data.local.dao.*
import com.haramblur.app.data.local.database.HaramBlurDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideHaramBlurDatabase(@ApplicationContext context: Context): HaramBlurDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            HaramBlurDatabase::class.java,
            HaramBlurDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideDetectionDao(database: HaramBlurDatabase): DetectionDao {
        return database.detectionDao()
    }
    
    @Provides
    fun provideBlockedSiteDao(database: HaramBlurDatabase): BlockedSiteDao {
        return database.blockedSiteDao()
    }
    
    @Provides
    fun provideIslamicReminderDao(database: HaramBlurDatabase): IslamicReminderDao {
        return database.islamicReminderDao()
    }
    
    @Provides
    fun provideCodeRedSessionDao(database: HaramBlurDatabase): CodeRedSessionDao {
        return database.codeRedSessionDao()
    }
}