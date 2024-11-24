/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.source.transformer

import com.weather.nimbus.data.weather.model.CurrentWeatherData
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse

class CurrentWeatherResponseTransformer {
    fun transform(response: CurrentWeatherResponse): CurrentWeatherData {

        return CurrentWeatherData(
            cityName = response.cityName,
            date = response.date,
            timezone = response.timezone,
            weather = transformWeather(response.weather.first()),
            main = transformMain(response.main)
        )
    }

    private fun transformWeather(
        weather: CurrentWeatherResponse.Weather
    ): CurrentWeatherData.Weather {
        val weatherStatus = when(weather.id) {
            in 200..232 -> "thunderstorm"
            in 300..321 -> "rain"
            in 500..531 -> "rain"
            in 600..622 -> "snow"
            in 701..781 -> "atmosphere"
            800 -> "clear"
            in 801..804 -> "clouds"
            else -> "clear"
        }
        val description = weather.description
        return CurrentWeatherData.Weather(
            weatherStatus,
            description
        )
    }

    private fun transformMain(main: CurrentWeatherResponse.Main): CurrentWeatherData.Main {
        return CurrentWeatherData.Main(
            temperature = main.temperature,
            feelsLike = main.feelsLike,
            minTemperature = main.minTemperature,
            maxTemperature = main.maxTemperature,
            pressure = main.pressure,
            humidity = main.humidity,
            seaLevel = main.seaLevel,
            groundLevel = main.groundLevel
        )
    }
}