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
    val mainConditions: Main,
    val wind: Wind,
    val clouds: Int
) {
    data class Weather(
        val weatherStatus: WeatherStatus,
        val description: String
    )

    data class Main(
        val temperature: Int,
        val feelsLike: Int,
        val minTemperature: Int,
        val maxTemperature: Int,
        val pressure: Int,
        val humidity: Int
    )

    data class Wind(
        val speed: Double,
        val speedClassification: String,
        val direction: String,
        val degrees: Int
    )
}
