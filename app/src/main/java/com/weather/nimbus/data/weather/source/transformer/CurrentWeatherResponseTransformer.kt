/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.source.transformer

import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse

class CurrentWeatherResponseTransformer {
    fun transform(response: CurrentWeatherResponse): WeatherData {

        return WeatherData(
            cityName = response.cityName,
            date = response.date,
            timezone = response.timezone,
            weather = transformWeather(response.weather.first()),
            main = transformMain(response.main)
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
            in 701..781 -> WeatherStatus.THUNDERSTORM
            in 801..804 -> WeatherStatus.CLOUDS
            else -> WeatherStatus.CLEAR
        }
        val description = weather.description
        return WeatherData.Weather(
            weatherStatus,
            description
        )
    }

    private fun transformMain(main: CurrentWeatherResponse.Main): WeatherData.Main {
        return WeatherData.Main(
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