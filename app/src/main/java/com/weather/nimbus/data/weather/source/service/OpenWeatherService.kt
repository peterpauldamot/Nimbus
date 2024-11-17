/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.data.weather.network.api

import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse

interface OpenWeatherService {
    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): CurrentWeatherResponse

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): FiveDayForecastResponse
}