/*
 * Created by Peter Paul Damot on 11-23-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.domain.weather

import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.source.repository.OpenWeatherRepository

class GetCurrentWeatherUseCase(private val weatherRepository: OpenWeatherRepository) {
    suspend operator fun invoke(latitude: String, longitude: String): CurrentWeatherResponse {
        return weatherRepository.getCurrentWeather(latitude, longitude)
    }
}