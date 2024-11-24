/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.CurrentWeatherData
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse
import com.weather.nimbus.domain.cities.GetCitiesUseCase
import com.weather.nimbus.domain.location.GetCurrentLocationUseCase
import com.weather.nimbus.domain.weather.GetCurrentWeatherUseCase
import com.weather.nimbus.domain.weather.GetFiveDayForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeather: GetCurrentWeatherUseCase,
    private val getFiveDayForecast: GetFiveDayForecastUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val getCities: GetCitiesUseCase
) : ViewModel() {
    private val _weatherData = MutableStateFlow<CurrentWeatherData?>(null)
    val weatherData: StateFlow<CurrentWeatherData?> = _weatherData

    private val _forecastData = MutableStateFlow<FiveDayForecastResponse?>(null)
    val forecastData: StateFlow<FiveDayForecastResponse?> = _forecastData

    private val _cityData = MutableStateFlow<List<CitiesResponse>>(emptyList())
    val cityData: StateFlow<List<CitiesResponse?>> = _cityData

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getCurrentWeather() {
        viewModelScope.launch {
            val location = getCurrentLocation()
            location?.let {
                val latitude = it.latitude.toString()
                val longitude = it.longitude.toString()

                Log.d("WeatherViewModel",
                    "Fetching weather data for latitude: $latitude, longitude: $longitude")
                val result = runCatching {
                    getCurrentWeather(latitude, longitude)
                }

                result.onSuccess { response ->
                    _weatherData.value = response.getOrNull()
                    Log.d("WeatherViewModel", "Updated weatherData value.")
                }.onFailure { exception ->
                    exception.printStackTrace()
                    Log.e("WeatherViewModel", "Error fetching weather data.")
                }
            }?: run {
                _errorState.value = "Location not found"
                Log.e("WeatherViewModel", "Error fetching location: Location is null")
            }
        }
    }

    fun getForecastWeather() {
        viewModelScope.launch {
            val location = getCurrentLocation()
            location?.let {
                val latitude = it.latitude.toString()
                val longitude = it.longitude.toString()

                Log.d("WeatherViewModel",
                    "Fetching forecast data for latitude: $latitude, longitude: $longitude")
                val result = runCatching {
                    getFiveDayForecast(latitude, longitude)
                }

                result.onSuccess { response ->
                    _forecastData.value = response.getOrNull()
                    Log.d("WeatherViewModel", "Updated forecastData value.")
                }.onFailure { exception ->
                    exception.printStackTrace()
                    Log.e("WeatherViewModel", "Error forecast weather data.")
                }
            }?: run {
                _errorState.value = "Location not found"
                Log.e("WeatherViewModel", "Error fetching location: Location is null")
            }
        }
    }

    fun getCurrentWeatherOnCity(lat: Double?, long: Double?) {
        val latitude = lat.toString()
        val longitude = long.toString()
        if (lat != null && long != null) {
            viewModelScope.launch {
                Log.d("WeatherViewModel",
                    "Fetching weather data for latitude: $latitude, longitude: $longitude")
                val result = runCatching {
                    getCurrentWeather(latitude, longitude)
                }

                result.onSuccess { response ->
                    _weatherData.value = response.getOrNull()
                    Log.d("WeatherViewModel", "Updated weatherData value.")
                }.onFailure { exception ->
                    exception.printStackTrace()
                    Log.e("WeatherViewModel", "Error fetching weather data.")
                }
            }
        }
    }

    fun getCityData() {
        viewModelScope.launch {
            val result = runCatching {
                Log.d("WeatherViewModel", "Fetching cities...")
                getCities()
            }

            result.onSuccess { json ->
                _cityData.value = json.getOrThrow()
                Log.d("WeatherViewModel", "Updated cityData value.")
            }.onFailure { exception ->
                exception.printStackTrace()
                Log.e("WeatherViewModel", "Error fetching city data.")
            }
        }
    }
}