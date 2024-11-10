/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.data.network.api

import com.weather.nimbus.data.model.CurrentWeatherResponse
import com.weather.nimbus.data.model.FiveDayForecastResponse

class OpenWeatherServiceImpl(private val openWeatherAPI: OpenWeatherAPI) : OpenWeatherService{
    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String,
        apiKey: String
    ): CurrentWeatherResponse {
        return openWeatherAPI.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
    }

    override suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String,
        apiKey: String
    ): FiveDayForecastResponse {
        return openWeatherAPI.getFiveDayForecast(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
    }
}