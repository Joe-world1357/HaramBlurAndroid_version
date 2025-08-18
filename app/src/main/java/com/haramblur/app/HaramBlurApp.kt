package com.haramblur.app

import android.app.Application
import com.haramblur.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Main Application class for HaramBlur
 */
class HaramBlurApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.ERROR) // Use ERROR level to avoid Koin Android logger issues
            androidContext(this@HaramBlurApp)
            modules(appModule)
        }
    }
}
