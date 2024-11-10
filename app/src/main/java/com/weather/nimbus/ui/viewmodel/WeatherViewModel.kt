/*
 * Created by Peter Paul Damot on 2024-11-10.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.model.CurrentWeatherResponse
import com.weather.nimbus.data.network.api.OpenWeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val openWeatherService: OpenWeatherService) : ViewModel() {
    // A MutableStateFlow to hold the weather data state
    private val _weatherData = MutableStateFlow<CurrentWeatherResponse?>(null)
    val weatherData: StateFlow<CurrentWeatherResponse?> = _weatherData

    // A MutableStateFlow for managing error states
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    // Function to fetch current weather
    fun getCurrentWeather(lat: String, long: String, key: String) {
        viewModelScope.launch {
            try {
                val response = openWeatherService.getCurrentWeather(
                    latitude = lat,
                    longitude = long,
                    apiKey = key)
                _weatherData.value = response // Update the StateFlow with new data
                Log.d("WeatherViewModel", "Weather data updated: $response")
            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.value = e.message // Update the StateFlow with the error
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }
}

/*
 * MutableStateFlow holds mutable data for weatherData and errorState.
 * weatherData and errorState are public as StateFlow types so they’re read-only in the UI layer.
 * getCurrentWeather updates _weatherData on successful API calls and _errorState in case of an exception.
 */