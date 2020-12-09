package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
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
    @GET("neo/rest/v1/feed?api_key=g42Sxb6P8Q51UWdSVY0xPK9TDGJX0PkGTSbfG60J")
fun getAsteroids(@Query("start_date") startDate: String = "",
                 @Query("end_date") endDate: String = ""):
            Call<String>
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}