package com.haramblur.app.data.initializer

import com.haramblur.app.domain.repository.IslamicReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Initializes the database with default data
 */
@Singleton
class DatabaseInitializer @Inject constructor(
    private val islamicReminderRepository: IslamicReminderRepository
) {
    
    private val initializerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    /**
     * Initialize the database with default data
     */
    fun initialize() {
        initializerScope.launch {
            try {
                // Initialize Islamic reminders with default content
                islamicReminderRepository.initializeDefaultReminders()
            } catch (e: Exception) {
                // Log error but don't crash the app
                // In a production app, we would use proper logging
            }
        }
    }
}