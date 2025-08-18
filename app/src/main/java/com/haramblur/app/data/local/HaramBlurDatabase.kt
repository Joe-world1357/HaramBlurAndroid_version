package com.haramblur.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1,
    exportSchema = true
)
abstract class HaramBlurDatabase : RoomDatabase()

