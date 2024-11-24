/*
 * Created by Peter Paul Damot on 11-25-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-25-2024.
 */

package com.weather.nimbus.data.weather.source.transformer

import com.weather.nimbus.data.weather.model.FiveDayForecastResponse
import com.weather.nimbus.data.weather.model.ForecastData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FiveDayForecastResponseTransformer {
    companion object {
        const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    }

    fun transform(response: FiveDayForecastResponse): ForecastData {
        val groupedForecast = groupForecastByDate(response.forecast)

        val dailyForecasts = groupedForecast.map { (date, dailyForecast) ->
            val averageMinTemperature = calculateDailyAverageMin(dailyForecast)
            val averageMaxTemperature = calculateDailyAverageMax(dailyForecast)
            val dayOfWeek = getDayOfWeek(date)
            ForecastData.Forecast(
                date = date,
                averageMinTemperature = averageMinTemperature,
                averageMaxTemperature = averageMaxTemperature,
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

    private fun calculateDailyAverageMin(forecasts: List<FiveDayForecastResponse.Forecast>): Double {
        return forecasts.map { it.main.minTemperature }.average()
    }

    private fun calculateDailyAverageMax(forecasts: List<FiveDayForecastResponse.Forecast>): Double {
        return forecasts.map { it.main.maxTemperature }.average()
    }

    private fun getDayOfWeek(timestamp: Int): String {
        val date = Date(timestamp.toLong() * 1000)

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dayFormat.format(date)
    }
}