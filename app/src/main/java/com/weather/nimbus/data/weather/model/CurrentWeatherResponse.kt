/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.data.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val cityName: String,
    @SerialName("dt") val date: Int,
    @SerialName("timezone") val timezone: Int,
    @SerialName("weather") val weather: List<Weather>,
    @SerialName("main") val main: Main,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind") val wind: Wind,
    @SerialName("clouds") val clouds: Clouds,
    @SerialName("rain") val rain: Precipitation? = null,
    @SerialName("snow") val snow: Precipitation? = null
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
        @SerialName("speed") val speed: Double? = null,
        @SerialName("deg") val degrees: Int? = null,
        @SerialName("gust") val gust: Double? = null
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