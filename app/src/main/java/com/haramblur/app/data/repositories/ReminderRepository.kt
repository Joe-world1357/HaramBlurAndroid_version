package com.haramblur.app.data.repositories

import com.haramblur.app.data.dao.IslamicReminderDao
import com.haramblur.app.data.models.IslamicReminder
import com.haramblur.app.data.models.ReminderCategory
import com.haramblur.app.data.models.entities.toDomainModel
import com.haramblur.app.data.models.entities.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for Islamic reminders
 */
class ReminderRepository(private val reminderDao: IslamicReminderDao) {

    /**
     * Get a random reminder
     */
    suspend fun getRandomReminder(): IslamicReminder? {
        return reminderDao.getRandomReminder()?.toDomainModel()
    }

    /**
     * Get a random reminder by category
     */
    suspend fun getRandomReminderByCategory(category: ReminderCategory): IslamicReminder? {
        return reminderDao.getRandomReminderByCategory(category.name)?.toDomainModel()
    }

    /**
     * Get all reminders
     */
    fun getAllReminders(): Flow<List<IslamicReminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Get reminders by category
     */
    fun getRemindersByCategory(category: ReminderCategory): Flow<List<IslamicReminder>> {
        return reminderDao.getRemindersByCategory(category.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    /**
     * Add a new reminder
     */
    suspend fun addReminder(quote: String, source: String, category: ReminderCategory): Long {
        val reminder = IslamicReminder(
            quote = quote,
            source = source,
            category = category
        )
        return reminderDao.insert(reminder.toEntity())
    }

    /**
     * Delete a reminder
     */
    suspend fun deleteReminder(reminderId: Long) {
        val reminder = reminderDao.getById(reminderId)
        reminder?.let {
            reminderDao.delete(it)
        }
    }
}
