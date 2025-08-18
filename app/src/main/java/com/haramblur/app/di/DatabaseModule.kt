package com.haramblur.app.di

import android.content.Context
import androidx.room.Room
import com.haramblur.app.data.local.HaramBlurDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HaramBlurDatabase =
        Room.databaseBuilder(
            context,
            HaramBlurDatabase::class.java,
            "haramblur.db"
        ).fallbackToDestructiveMigration().build()
}

