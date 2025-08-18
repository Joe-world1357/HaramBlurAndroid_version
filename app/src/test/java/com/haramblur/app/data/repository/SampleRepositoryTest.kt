package com.haramblur.app.data.repository

import com.haramblur.app.data.remote.SampleApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SampleRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api: SampleApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SampleApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetchStatus returns success on 200`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200).setBody("OK"))
        val repository = SampleRepository(api)
        val result = repository.fetchStatus()
        assertTrue(result.isSuccess)
        assertEquals("OK", result.getOrNull())
    }
}

