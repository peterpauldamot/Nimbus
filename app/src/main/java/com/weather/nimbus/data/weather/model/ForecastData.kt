/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.model

import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse.City

data class ForecastData(
    val city: City,
    val weatherForecast: List<Forecast>
) {
    data class Forecast(
        val date: Int,
        val dayOfWeek: String,
        val weatherStatus: WeatherStatus,
        val minTemperature: Double,
        val maxTemperature: Double
    )
}
