package com.haramblur.app.data.remote

import retrofit2.http.GET

interface SampleApi {
    @GET("status")
    suspend fun getStatus(): String
}

