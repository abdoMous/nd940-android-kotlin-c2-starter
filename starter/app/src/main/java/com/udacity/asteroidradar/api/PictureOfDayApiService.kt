package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface PictureOfDayApiService {

    @GET("planetary/apod")
    fun getPictureOfDay(@Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY): Call<PictureOfDay>
}

object PictureOfDayApi {
    val retrofitService: PictureOfDayApiService by lazy {
        retrofit.create(PictureOfDayApiService::class.java)
    }
}