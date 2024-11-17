/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weather.nimbus.data.weather.network.api.OpenWeatherService
import com.weather.nimbus.domain.location.LocationUseCase

class WeatherViewModelFactory (
    private val openWeatherService: OpenWeatherService,
    private val locationUseCase: LocationUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  WeatherViewModel(openWeatherService, locationUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
