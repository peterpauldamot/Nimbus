/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.source.transformer

import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse
import com.weather.nimbus.data.weather.model.ForecastData
import com.weather.nimbus.data.weather.model.WeatherData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FiveDayForecastResponseTransformer {
    companion object {
        const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    }

    fun transform(response: FiveDayForecastResponse): ForecastData {
        val groupedForecast = groupForecastByDate(response.forecast)

        val dailyForecasts = groupedForecast.map { (date, dailyForecast) ->
            val weatherStatus = getDailyWeatherStatus(dailyForecast)
            val minTemperature = getDailyLowestTemperature(dailyForecast)
            val maxTemperature = getDailyHighestTemperature(dailyForecast)
            val dayOfWeek = getDayOfWeek(date)
            ForecastData.Forecast(
                date = date,
                weatherStatus = weatherStatus,
                minTemperature = minTemperature,
                maxTemperature = maxTemperature,
                dayOfWeek = dayOfWeek
            )
        }

        val forecastForNextFiveDays = if (dailyForecasts.size > 5) {
            // Drop the forecast for the same day
            dailyForecasts.drop(1)
        } else {
            dailyForecasts
        }

        return ForecastData(
            city = response.city,
            weatherForecast = forecastForNextFiveDays
        )
    }

    private fun groupForecastByDate(
        forecasts: List<FiveDayForecastResponse.Forecast>
    ): Map<Int, List<FiveDayForecastResponse.Forecast>> {
        val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        return forecasts.groupBy { forecast ->
            val date = formatter.parse(forecast.date)
            val timestampMilliseconds = date?.time
            val timestampSeconds = timestampMilliseconds?.div(1000)?.toInt()
            timestampSeconds ?: 0
        }
    }

    private fun getDailyLowestTemperature(forecasts: List<FiveDayForecastResponse.Forecast>): Double {
        return forecasts.minOfOrNull { it.main.minTemperature } ?: 0.0
    }

    private fun getDailyHighestTemperature(forecasts: List<FiveDayForecastResponse.Forecast>): Double {
        return forecasts.maxOfOrNull { it.main.maxTemperature } ?: 0.0
    }

    private fun getDailyWeatherStatus(forecasts: List<FiveDayForecastResponse.Forecast>): WeatherStatus {
        val targetHour = 12
        val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        val forecastAtNoon = forecasts.minByOrNull { forecast ->
            val date = formatter.parse(forecast.date)
            val calendar = Calendar.getInstance().apply { time = date!! }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            kotlin.math.abs(hour - targetHour)
        }
        val weatherStatus = transformWeather(forecastAtNoon?.weather?.first())
        return weatherStatus
    }

    private fun transformWeather(
        weather: FiveDayForecastResponse.Weather?
    ): WeatherStatus {
        val weatherStatus = when(weather?.id) {
            in 200..232 -> WeatherStatus.THUNDERSTORM
            in 300..321 -> WeatherStatus.DRIZZLE
            in 500..531 -> WeatherStatus.RAIN
            in 600..622 -> WeatherStatus.SNOW
            in 701..781 -> WeatherStatus.ATMOSPHERE
            in 801..804 -> WeatherStatus.CLOUDS
            else -> WeatherStatus.CLEAR
        }
        return weatherStatus
    }

    private fun getDayOfWeek(timestamp: Int): String {
        val date = Date(timestamp.toLong() * 1000)

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dayFormat.format(date)
    }
}