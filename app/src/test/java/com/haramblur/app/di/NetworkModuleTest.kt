package com.haramblur.app.di

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Retrofit

class NetworkModuleTest {

    @Test
    fun `provides Moshi, OkHttp and Retrofit`() {
        val moshi: Moshi = NetworkModule.provideMoshi()
        val client: OkHttpClient = NetworkModule.provideOkHttpClient(NetworkModule.provideLoggingInterceptor())
        val retrofit: Retrofit = NetworkModule.provideRetrofit(moshi, client)

        assertNotNull(moshi)
        assertNotNull(client)
        assertNotNull(retrofit)
    }
}

