package com.haramblur.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.haramblur.app.data.local.dao.*
import com.haramblur.app.data.local.entity.*

/**
 * Room database for HaramBlur app
 */
@Database(
    entities = [
        DetectionEntity::class,
        BlockedSiteEntity::class,
        IslamicReminderEntity::class,
        CodeRedSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HaramBlurDatabase : RoomDatabase() {
    
    abstract fun detectionDao(): DetectionDao
    abstract fun blockedSiteDao(): BlockedSiteDao
    abstract fun islamicReminderDao(): IslamicReminderDao
    abstract fun codeRedSessionDao(): CodeRedSessionDao
    
    companion object {
        const val DATABASE_NAME = "haramblur_database"
        
        @Volatile
        private var INSTANCE: HaramBlurDatabase? = null
        
        fun getDatabase(context: Context): HaramBlurDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HaramBlurDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}