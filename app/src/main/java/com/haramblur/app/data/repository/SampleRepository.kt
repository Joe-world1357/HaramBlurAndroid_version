package com.haramblur.app.data.repository

import com.haramblur.app.data.remote.SampleApi
import javax.inject.Inject

class SampleRepository @Inject constructor(
    private val sampleApi: SampleApi
) {
    suspend fun fetchStatus(): Result<String> = try {
        Result.success(sampleApi.getStatus())
    } catch (t: Throwable) {
        Result.failure(t)
    }
}

