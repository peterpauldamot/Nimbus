/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weather.nimbus.data.network.api.OpenWeatherService

class WeatherViewModelFactory (
    private val openWeatherService: OpenWeatherService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return  WeatherViewModel(openWeatherService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
