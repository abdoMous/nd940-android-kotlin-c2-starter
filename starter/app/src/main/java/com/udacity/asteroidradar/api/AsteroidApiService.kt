package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidApiFilter(val value: String) {
    SHOW_WEEK_ASTEROIDS("week"),
    SHOW_TODAY_ASTEROIDS("today"),
    SHOW_SAVED_ASTEROIDS("saved") }

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY):
                Call<String>
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}