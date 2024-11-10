/*
 * Created by Peter Paul Damot on 2024-11-09.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-09.
 */

package com.weather.nimbus.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val cityName: Int,
    @SerialName("dt") val date: Int,
    @SerialName("timezone") val timezone: Int,
    @SerialName("weather") val weather: List<Weather>,
    @SerialName("main") val main: Main,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind") val wind: Wind,
    @SerialName("clouds") val clouds: Clouds,
    @SerialName("rain") val rain: Precipitation?,
    @SerialName("snow") val snow: Precipitation?
) {
    @Serializable
    data class Weather(
        @SerialName("id") val id: Int,
        @SerialName("main") val main: String,
        @SerialName("description") val description: String
    )

    @Serializable
    data class Main(
        @SerialName("temp") val temperature: Double,
        @SerialName("feels_like") val feelsLike: Double,
        @SerialName("temp_min") val minTemperature: Double,
        @SerialName("temp_max") val maxTemperature: Double,
        @SerialName("pressure") val pressure: Int,
        @SerialName("humidity") val humidity: Int,
        @SerialName("sea_level") val seaLevel: Int,
        @SerialName("grnd_level") val groundLevel: Int
    )

    @Serializable
    data class Wind(
        @SerialName("speed") val speed: Double,
        @SerialName("deg") val degrees: Int,
        @SerialName("gust") val gust: Double
    )

    @Serializable
    data class Clouds(
        @SerialName("all") val cloudPercentage: Int
    )

    @Serializable
    data class Precipitation(
        @SerialName("1h") val precipitation: Double
    )
}