/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.source.transformer

import com.weather.nimbus.common.model.TemperatureUnit
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import kotlin.math.roundToInt

class CurrentWeatherResponseTransformer {
    fun transform(
        response: CurrentWeatherResponse,
        temperatureUnit: TemperatureUnit
    ): WeatherData {
        return WeatherData(
            cityName = response.cityName,
            date = response.date,
            timezone = response.timezone,
            weather = transformWeather(response.weather.first()),
            mainConditions = transformMain(response.main, temperatureUnit),
            wind = transformWind(response.wind),
            clouds = response.clouds.cloudPercentage
        )
    }

    private fun transformWeather(
        weather: CurrentWeatherResponse.Weather
    ): WeatherData.Weather {
        val weatherStatus = when(weather.id) {
            in 200..232 -> WeatherStatus.THUNDERSTORM
            in 300..321 -> WeatherStatus.DRIZZLE
            in 500..531 -> WeatherStatus.RAIN
            in 600..622 -> WeatherStatus.SNOW
            in 701..781 -> WeatherStatus.ATMOSPHERE
            in 801..804 -> WeatherStatus.CLOUDS
            else -> WeatherStatus.CLEAR
        }
        val description = capitalizeFirstLetter(weather.description)
        return WeatherData.Weather(
            weatherStatus,
            description
        )
    }

    private fun transformMain(
        main: CurrentWeatherResponse.Main,
        temperatureUnit: TemperatureUnit
    ): WeatherData.Main {
        return WeatherData.Main(
            temperature = convertTemperature(main.temperature, temperatureUnit),
            feelsLike = convertTemperature(main.feelsLike, temperatureUnit),
            minTemperature = convertTemperature(main.minTemperature, temperatureUnit),
            maxTemperature = convertTemperature(main.maxTemperature, temperatureUnit),
            pressure = main.pressure,
            humidity = main.humidity
        )
    }
    
    private fun transformWind(wind: CurrentWeatherResponse.Wind) : WeatherData.Wind {
        val speed = wind.speed
        val speedClassification = when {
            speed < 5 -> "Light"
            speed in 5.0..10.0 -> "Moderate"
            speed in 10.0..20.0 -> "Strong"
            speed in 20.0..30.0 -> "Gale"
            speed in 30.0..40.0 -> "Storm"
            else -> "Hurricane"
        }

        val direction = when (wind.degrees) {
            in 0..22 -> "North"
            in 23..67 -> "North-East"
            in 68..112 -> "East"
            in 113..157 -> "South-East"
            in 158..202 -> "South"
            in 203..247 -> "South-West"
            in 248..292 -> "West"
            in 293..337 -> "North-West"
            in 338..360 -> "North"
            else -> "Unknown"
        }

        return WeatherData.Wind(
            speed = speed,
            speedClassification = speedClassification,
            direction = direction,
            degrees = wind.degrees
        )
    }

    private fun convertTemperature(kelvin: Double, unit: TemperatureUnit): Int {
        return when (unit) {
            TemperatureUnit.CELSIUS -> (kelvin - 273.15).roundToInt()
            TemperatureUnit.FAHRENHEIT -> ((kelvin - 273.15) * 9 / 5 + 32).roundToInt()
            TemperatureUnit.KELVIN -> kelvin.roundToInt()
        }
    }

    private fun capitalizeFirstLetter(input: String?): String {
        if (input == null) return ""
        return input
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}