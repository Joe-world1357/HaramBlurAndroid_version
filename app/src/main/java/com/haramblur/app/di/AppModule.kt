package com.haramblur.app.di

import com.haramblur.app.data.AppDatabase
import com.haramblur.app.data.repositories.CodeRedRepository
import com.haramblur.app.data.repositories.DetectionRepository
import com.haramblur.app.data.repositories.ReminderRepository
import com.haramblur.app.viewmodels.CodeRedViewModel
import com.haramblur.app.viewmodels.HomeViewModel
import com.haramblur.app.viewmodels.SettingsViewModel
import com.haramblur.app.viewmodels.StatsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for dependency injection
 */
val appModule = module {
    // Database
    single { AppDatabase.getDatabase(androidContext()) }
    
    // DAOs
    single { get<AppDatabase>().detectionEventDao() }
    single { get<AppDatabase>().lockSessionDao() }
    single { get<AppDatabase>().lockPresetDao() }
    single { get<AppDatabase>().islamicReminderDao() }
    
    // Repositories
    single { DetectionRepository(get()) }
    single { CodeRedRepository(get(), get(), androidContext()) }
    single { ReminderRepository(get()) }
    
    // ViewModels
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CodeRedViewModel(get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { SettingsViewModel(androidContext()) }
}
