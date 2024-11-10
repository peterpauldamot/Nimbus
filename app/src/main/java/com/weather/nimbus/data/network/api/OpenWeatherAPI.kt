/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.data.network.api

import com.weather.nimbus.data.model.CurrentWeatherResponse
import com.weather.nimbus.data.model.FiveDayForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String)
    : CurrentWeatherResponse

    @GET("forecast")
    suspend fun  getFiveDayForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String)
    : FiveDayForecastResponse
}