/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.data.weather.source.repository

import com.weather.nimbus.common.model.TemperatureUnit
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.data.weather.model.ForecastData

interface OpenWeatherRepository {
    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String,
        temperatureUnit: TemperatureUnit
    ): Result<WeatherData>

    suspend fun getFiveDayForecast(
        latitude: String,
        longitude: String,
        temperatureUnit: TemperatureUnit
    ): Result<ForecastData>
}