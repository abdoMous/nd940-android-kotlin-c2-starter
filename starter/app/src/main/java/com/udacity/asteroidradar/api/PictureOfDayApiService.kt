package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface PictureOfDayApiService {
    @GET("planetary/apod?api_key=g42Sxb6P8Q51UWdSVY0xPK9TDGJX0PkGTSbfG60J")
    fun getPictureOfDay(): Call<PictureOfDay>
}

object PictureOfDayApi {
    val retrofitService: PictureOfDayApiService by lazy {
        retrofit.create(PictureOfDayApiService::class.java)
    }
}