package com.haramblur.app

import android.app.Application
import com.haramblur.app.data.initializer.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for HaramBlur app
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class HaramBlurApplication : Application() {
    
    @Inject
    lateinit var databaseInitializer: DatabaseInitializer
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize database with default data
        databaseInitializer.initialize()
    }
}