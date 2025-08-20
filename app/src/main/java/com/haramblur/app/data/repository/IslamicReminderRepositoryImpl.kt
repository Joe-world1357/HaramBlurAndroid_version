package com.haramblur.app.data.repository

import com.haramblur.app.data.local.dao.IslamicReminderDao
import com.haramblur.app.data.local.entity.toDomain
import com.haramblur.app.data.local.entity.toEntity
import com.haramblur.app.domain.model.IslamicReminder
import com.haramblur.app.domain.model.ReminderCategory
import com.haramblur.app.domain.model.ReminderType
import com.haramblur.app.domain.repository.IslamicReminderRepository
import com.haramblur.app.domain.repository.ReminderStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of IslamicReminderRepository
 */
@Singleton
class IslamicReminderRepositoryImpl @Inject constructor(
    private val islamicReminderDao: IslamicReminderDao
) : IslamicReminderRepository {
    
    override suspend fun insertReminder(reminder: IslamicReminder) {
        islamicReminderDao.insertReminder(reminder.toEntity())
    }
    
    override suspend fun updateReminder(reminder: IslamicReminder) {
        islamicReminderDao.updateReminder(reminder.toEntity())
    }
    
    override suspend fun deleteReminder(id: Int) {
        islamicReminderDao.deleteReminderById(id)
    }
    
    override fun getAllReminders(): Flow<List<IslamicReminder>> {
        return islamicReminderDao.getAllActiveReminders().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getRemindersByType(type: ReminderType): Flow<List<IslamicReminder>> {
        return islamicReminderDao.getRemindersByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getRemindersByCategory(category: ReminderCategory): Flow<List<IslamicReminder>> {
        return islamicReminderDao.getRemindersByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getFavoriteReminders(): Flow<List<IslamicReminder>> {
        return islamicReminderDao.getFavoriteReminders().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getRandomReminder(categories: List<ReminderCategory>): IslamicReminder? {
        return if (categories.isEmpty()) {
            islamicReminderDao.getRandomReminder()?.toDomain()
        } else {
            val categoryNames = categories.map { it.name }
            val excludeAfterTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000) // 24 hours ago
            islamicReminderDao.getRandomReminderByCategories(categoryNames, excludeAfterTime)?.toDomain()
        }
    }
    
    override suspend fun getContextualReminder(
        category: ReminderCategory,
        excludeRecent: Boolean,
        recentHours: Int
    ): IslamicReminder? {
        val excludeAfterTime = if (excludeRecent) {
            System.currentTimeMillis() - (recentHours * 60 * 60 * 1000)
        } else {
            0L
        }
        
        return islamicReminderDao.getRandomReminderByCategory(category.name, excludeAfterTime)?.toDomain()
    }
    
    override suspend fun markReminderShown(id: Int) {
        islamicReminderDao.markReminderShown(id, System.currentTimeMillis())
    }
    
    override suspend fun toggleFavorite(id: Int) {
        islamicReminderDao.toggleFavorite(id)
    }
    
    override suspend fun searchReminders(query: String): List<IslamicReminder> {
        return islamicReminderDao.searchReminders(query).map { it.toDomain() }
    }
    
    override suspend fun initializeDefaultReminders() {
        // Check if reminders already exist
        val existingCount = islamicReminderDao.getTotalRemindersCount()
        if (existingCount > 0) {
            return // Already initialized
        }
        
        // Initialize with default Islamic reminders
        val defaultReminders = getDefaultReminders()
        islamicReminderDao.insertReminders(defaultReminders.map { it.toEntity() })
    }
    
    override suspend fun getReminderStats(): ReminderStats {
        val totalReminders = islamicReminderDao.getTotalRemindersCount()
        val quranVerses = islamicReminderDao.getQuranVersesCount()
        val hadithCount = islamicReminderDao.getHadithCount()
        val duasCount = islamicReminderDao.getDuasCount()
        val favoriteCount = islamicReminderDao.getFavoriteCount()
        
        val mostShownCategoryResult = islamicReminderDao.getMostShownCategory()
        val mostShownCategory = mostShownCategoryResult?.let {
            try {
                ReminderCategory.valueOf(it.category)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        
        val totalUsage = islamicReminderDao.getTotalUsage() ?: 0
        val lastShownTime = islamicReminderDao.getLastShownTime()
        
        return ReminderStats(
            totalReminders = totalReminders,
            quranVerses = quranVerses,
            hadithCount = hadithCount,
            duasCount = duasCount,
            favoriteCount = favoriteCount,
            mostShownCategory = mostShownCategory,
            totalUsage = totalUsage,
            lastShownTime = lastShownTime
        )
    }
    
    /**
     * Get default Islamic reminders for initialization
     */
    private fun getDefaultReminders(): List<IslamicReminder> {
        return listOf(
            // Quran verses about lowering gaze
            IslamicReminder(
                id = 1,
                type = ReminderType.QURAN,
                category = ReminderCategory.LOWERING_GAZE,
                arabicText = "قُل لِّلْمُؤْمِنِينَ يَغُضُّوا مِنْ أَبْصَارِهِمْ وَيَحْفَظُوا فُرُوجَهُمْ ۚ ذَٰلِكَ أَزْكَىٰ لَهُمْ ۗ إِنَّ اللَّهَ خَبِيرٌ بِمَا يَصْنَعُونَ",
                englishTranslation = "Tell the believing men to reduce [some] of their vision and guard their private parts. That is purer for them. Indeed, Allah is Acquainted with what they do.",
                sourceReference = "Quran 24:30",
                themeTags = listOf("modesty", "purity", "gaze")
            ),
            
            IslamicReminder(
                id = 2,
                type = ReminderType.QURAN,
                category = ReminderCategory.LOWERING_GAZE,
                arabicText = "وَقُل لِّلْمُؤْمِنَاتِ يَغْضُضْنَ مِنْ أَبْصَارِهِنَّ وَيَحْفَظْنَ فُرُوجَهُنَّ",
                englishTranslation = "And tell the believing women to reduce [some] of their vision and guard their private parts.",
                sourceReference = "Quran 24:31",
                themeTags = listOf("modesty", "purity", "gaze")
            ),
            
            // Quran verse about purity
            IslamicReminder(
                id = 3,
                type = ReminderType.QURAN,
                category = ReminderCategory.PURITY,
                arabicText = "وَلَا تَقْرَبُوا الزِّنَا ۖ إِنَّهُ كَانَ فَاحِشَةً وَسَاءَ سَبِيلًا",
                englishTranslation = "And do not approach unlawful sexual intercourse. Indeed, it is ever an immorality and is evil as a way.",
                sourceReference = "Quran 17:32",
                themeTags = listOf("purity", "morality", "prohibition")
            ),
            
            // Hadith about controlling desires
            IslamicReminder(
                id = 4,
                type = ReminderType.HADITH,
                category = ReminderCategory.LOWERING_GAZE,
                arabicText = "النَّظْرَةُ سَهْمٌ مَسْمُومٌ مِنْ سِهَامِ إِبْلِيسَ",
                englishTranslation = "The glance is a poisoned arrow from the arrows of Satan.",
                narrator = "Ali ibn Abi Talib",
                sourceReference = "Al-Hakim",
                themeTags = listOf("gaze", "temptation", "protection")
            ),
            
            // Dua for protection
            IslamicReminder(
                id = 5,
                type = ReminderType.DUA,
                category = ReminderCategory.SEEKING_FORGIVENESS,
                arabicText = "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِنْ لَدُنْكَ رَحْمَةً ۚ إِنَّكَ أَنْتَ الْوَهَّابُ",
                englishTranslation = "Our Lord, let not our hearts deviate after You have guided us and grant us from Yourself mercy. Indeed, You are the Bestower.",
                sourceReference = "Quran 3:8",
                themeTags = listOf("guidance", "protection", "mercy")
            ),
            
            // More reminders about Taqwa
            IslamicReminder(
                id = 6,
                type = ReminderType.QURAN,
                category = ReminderCategory.TAQWA,
                arabicText = "وَمَنْ يَتَّقِ اللَّهَ يَجْعَلْ لَهُ مَخْرَجًا",
                englishTranslation = "And whoever fears Allah - He will make for him a way out.",
                sourceReference = "Quran 65:2",
                themeTags = listOf("taqwa", "solution", "trust")
            )
        )
    }
}