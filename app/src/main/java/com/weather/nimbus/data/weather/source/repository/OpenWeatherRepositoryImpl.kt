/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.data.weather.source.repository

import android.util.Log
import com.weather.nimbus.data.weather.model.CurrentWeatherData
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse
import com.weather.nimbus.data.weather.source.api.OpenWeatherAPI
import com.weather.nimbus.data.weather.source.transformer.CurrentWeatherResponseTransformer
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val openWeatherAPI: OpenWeatherAPI,
    private val currentWeatherTransformer: CurrentWeatherResponseTransformer
) : OpenWeatherRepository {
    companion object {
        const val OPEN_WEATHER_API_KEY = "2f1894939b86e62241429f38569bef0e"
    }

    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Result<CurrentWeatherData> {
        return runCatching {
            Log.d("OpenWeatherRepository", "Fetching current weather data...")
            val response = openWeatherAPI.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
                apiKey = OPEN_WEATHER_API_KEY
            )
            Log.d("OpenWeatherRepository", "Success fetching current weather data: $response")

            val weatherData = currentWeatherTransformer.transform(response = response)
            Result.success(weatherData)
        }.getOrElse { exception ->
            Log.e("OpenWeatherRepository", "Error fetching current weather data", exception)
            Result.failure(exception)
        }
    }

    override suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): Result<FiveDayForecastResponse> {
        return runCatching {
            Log.d("OpenWeatherRepository", "Fetching forecast weather data...")
            val response = openWeatherAPI.getFiveDayForecast(
                latitude = latitude,
                longitude = longitude,
                apiKey = OPEN_WEATHER_API_KEY
            )
            Log.d("OpenWeatherRepository", "Success fetching forecast weather data: $response")
            Result.success(response)
        }.getOrElse { exception ->
            Log.e("OpenWeatherRepository", "Error fetching forecast weather data", exception)
            Result.failure(exception)
        }
    }
}