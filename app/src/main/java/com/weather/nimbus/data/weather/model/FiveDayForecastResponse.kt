/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.data.weather.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiveDayForecastResponse(
    @SerialName("cnt") val noOfTimestamps: Int,
    @SerialName("list") val forecast: List<Forecast>,
    @SerialName("city") val city: City
) {
    @Serializable
    data class Forecast(
        @SerialName("dt") val timeOfForecast: Int,
        @SerialName("main") val main: Main,
        @SerialName("weather") val weather: Weather,
        @SerialName("clouds") val clouds: Clouds,
        @SerialName("wind") val wind: Wind,
        @SerialName("visibility") val visibility: Int,
        @SerialName("pop") val precipitationProbability: Double,
        @SerialName("rain") val rain: PrecipitationDuration,
        @SerialName("snow") val snow: PrecipitationDuration,
        @SerialName("sys") val sys: Sys,
        @SerialName("dt_txt") val date: String

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
    data class Weather(
        @SerialName("id") val id: Int,
        @SerialName("main") val main: String,
        @SerialName("description") val description: String
    )

    @Serializable
    data class Clouds(
        @SerialName("all") val cloudPercentage: Int
    )

    @Serializable
    data class Wind(
        @SerialName("speed") val speed: Double,
        @SerialName("deg") val degrees: Int,
        @SerialName("gust") val gust: Double
    )

    @Serializable
    data class PrecipitationDuration(
        @SerialName("3h") val threeHour: Double
    )

    @Serializable
    data class Sys(
        @SerialName("pod") val partOfDay: String
    )

    @Serializable
    data class City(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("coord") val coordinates: Coordinates,
        @SerialName("country") val country: String,
        @SerialName("population") val population: Int,
        @SerialName("timezone") val timezone: Int,
        @SerialName("sunrise") val sunrise: Int,
        @SerialName("sunset") val sunset: Int
    )

    @Serializable
    data class Coordinates(
        @SerialName("lat") val latitude: Double,
        @SerialName("lon") val longitude: Double
    )
}


