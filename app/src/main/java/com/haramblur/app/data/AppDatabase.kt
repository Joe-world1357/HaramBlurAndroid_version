package com.haramblur.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.haramblur.app.data.dao.DetectionEventDao
import com.haramblur.app.data.dao.IslamicReminderDao
import com.haramblur.app.data.dao.LockPresetDao
import com.haramblur.app.data.dao.LockSessionDao
import com.haramblur.app.data.models.ReminderCategory
import com.haramblur.app.data.models.entities.Converters
import com.haramblur.app.data.models.entities.DetectionEventEntity
import com.haramblur.app.data.models.entities.IslamicReminderEntity
import com.haramblur.app.data.models.entities.LockPresetEntity
import com.haramblur.app.data.models.entities.LockSessionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main database for the application
 */
@Database(
    entities = [
        DetectionEventEntity::class,
        LockSessionEntity::class,
        LockPresetEntity::class,
        IslamicReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun detectionEventDao(): DetectionEventDao
    abstract fun lockSessionDao(): LockSessionDao
    abstract fun lockPresetDao(): LockPresetDao
    abstract fun islamicReminderDao(): IslamicReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "haramblur_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Populate database with initial data
                        CoroutineScope(Dispatchers.IO).launch {
                            populateDatabase(getDatabase(context))
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Populate database with initial data
         */
        private suspend fun populateDatabase(database: AppDatabase) {
            // Add default lock presets
            val presetDao = database.lockPresetDao()
            val defaultPresets = listOf(
                LockPresetEntity(
                    name = "Social Media",
                    apps = "com.instagram.android,com.facebook.katana,com.twitter.android,com.snapchat.android,com.tiktok.android",
                    durationMinutes = 15
                ),
                LockPresetEntity(
                    name = "Gaming",
                    apps = "com.supercell.clashofclans,com.pubg.imobile,com.ea.gp.fifamobile,com.activision.callofduty.shooter",
                    durationMinutes = 60
                ),
                LockPresetEntity(
                    name = "Entertainment",
                    apps = "com.netflix.mediaclient,com.amazon.avod.thirdpartyclient,com.disney.disneyplus,com.google.android.youtube",
                    durationMinutes = 30
                ),
                LockPresetEntity(
                    name = "Messaging",
                    apps = "com.whatsapp,com.facebook.orca,org.telegram.messenger,com.viber.voip,com.discord",
                    durationMinutes = 120
                )
            )
            presetDao.insertAll(defaultPresets)

            // Add Islamic reminders
            val reminderDao = database.islamicReminderDao()
            val defaultReminders = listOf(
                IslamicReminderEntity(
                    quote = "Say to the believing men that they should lower their gaze",
                    source = "Quran 24:30",
                    category = ReminderCategory.LOWERING_GAZE.name
                ),
                IslamicReminderEntity(
                    quote = "Every son of Adam commits sin, and the best of those who sin are those who repent",
                    source = "Ibn Majah",
                    category = ReminderCategory.GENERAL.name
                ),
                IslamicReminderEntity(
                    quote = "Indeed, with hardship comes ease",
                    source = "Quran 94:5",
                    category = ReminderCategory.PATIENCE.name
                ),
                IslamicReminderEntity(
                    quote = "Verily, Allah loves those who repent and those who purify themselves",
                    source = "Quran 2:222",
                    category = ReminderCategory.PURITY.name
                ),
                IslamicReminderEntity(
                    quote = "The strongest among you is the one who controls his anger",
                    source = "Bukhari",
                    category = ReminderCategory.SELF_CONTROL.name
                ),
                IslamicReminderEntity(
                    quote = "Establish prayer, for indeed, prayer prohibits immorality and wrongdoing",
                    source = "Quran 29:45",
                    category = ReminderCategory.PRAYER.name
                ),
                IslamicReminderEntity(
                    quote = "Lower your gaze and guard your modesty",
                    source = "Bukhari",
                    category = ReminderCategory.LOWERING_GAZE.name
                ),
                IslamicReminderEntity(
                    quote = "Indeed, Allah does not look at your appearance or wealth, but rather He looks at your hearts and actions",
                    source = "Muslim",
                    category = ReminderCategory.GENERAL.name
                )
            )
            reminderDao.insertAll(defaultReminders)
        }
    }
}
