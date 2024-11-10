/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.data.network.api

import com.weather.nimbus.data.model.CurrentWeatherResponse
import com.weather.nimbus.data.model.FiveDayForecastResponse

interface OpenWeatherService {
    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String,
        apiKey: String
    ): CurrentWeatherResponse

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String,
        apiKey: String
    ): FiveDayForecastResponse
}