/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.data.weather.source.repository

import com.weather.nimbus.data.weather.model.CurrentWeatherData
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse

interface OpenWeatherRepository {
    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Result<CurrentWeatherData>

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String
    ): Result<FiveDayForecastResponse>
}