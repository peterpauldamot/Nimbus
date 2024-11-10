/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.data.network

import com.weather.nimbus.data.network.api.OpenWeatherAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun create(): OpenWeatherAPI {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenWeatherAPI::class.java)
    }
}