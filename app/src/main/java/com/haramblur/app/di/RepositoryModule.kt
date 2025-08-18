package com.haramblur.app.di

import com.haramblur.app.data.remote.SampleApi
import com.haramblur.app.data.repository.SampleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSampleRepository(api: SampleApi): SampleRepository = SampleRepository(api)
}

