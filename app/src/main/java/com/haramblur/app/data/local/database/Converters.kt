package com.haramblur.app.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room type converters for complex data types
 */
class Converters {
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    @TypeConverter
    fun fromLong(value: Long?): Long? {
        return value
    }
    
    @TypeConverter
    fun toLong(value: Long?): Long? {
        return value
    }
}