/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.model

import com.weather.nimbus.common.model.WeatherStatus

data class WeatherData(
    val cityName: String,
    val date: Int,
    val timezone: Int,
    val weather: Weather,
    val main: Main
) {
    data class Weather(
        val weatherStatus: WeatherStatus,
        val description: String
    )

    data class Main(
        val temperature: Double,
        val feelsLike: Double,
        val minTemperature: Double,
        val maxTemperature: Double,
        val pressure: Int,
        val humidity: Int,
        val seaLevel: Int,
        val groundLevel: Int
    )
}
