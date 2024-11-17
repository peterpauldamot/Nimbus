/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.data.weather.network.api

import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse

class OpenWeatherServiceImpl(private val openWeatherAPI: OpenWeatherAPI) : OpenWeatherService {
    companion object {
        const val OPEN_WEATHER_API_KEY = "2f1894939b86e62241429f38569bef0e"
    }

    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): CurrentWeatherResponse {
        return openWeatherAPI.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            apiKey = OPEN_WEATHER_API_KEY
        )
    }

    override suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): FiveDayForecastResponse {
        return openWeatherAPI.getFiveDayForecast(
            latitude = latitude,
            longitude = longitude,
            apiKey = OPEN_WEATHER_API_KEY
        )
    }
}