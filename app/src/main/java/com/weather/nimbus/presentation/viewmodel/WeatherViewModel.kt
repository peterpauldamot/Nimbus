/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
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
    private val _weatherData = MutableStateFlow<CurrentWeatherResponse?>(null)
    val weatherData: StateFlow<CurrentWeatherResponse?> = _weatherData

    private val _forecastData = MutableStateFlow<FiveDayForecastResponse?>(null)
    val forecastData: StateFlow<FiveDayForecastResponse?> = _forecastData

    private val _cityData = MutableStateFlow<List<CitiesResponse>>(emptyList())
    val cityData: StateFlow<List<CitiesResponse?>> = _cityData

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getCurrentWeather() {
        viewModelScope.launch {
            try { // TODO: Replace with Run-catch
                Log.d("API", "Retrieving Location")
                val location = getCurrentLocation()
                location?.let {
                    Log.d("API", "Calling getCurrentWeather")
                    val response = getCurrentWeather(
                        latitude = it.latitude.toString(),
                        longitude = it.longitude.toString()
                    )
                    _weatherData.value = response
                    Log.d("API", "Weather data updated: $response")
                } ?: run {
                    _errorState.value = "Location not found"
                    Log.e("API", "Error fetching weather data: No Location")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.value = e.message
                Log.e("API", "Error fetching weather data:", e)
            }
        }
    }

    fun getForecastWeather(lat: String, long: String, key: String) {
        Log.d("API", "Executing getForecastWeather with lat:$lat, long:$long")
        viewModelScope.launch {
            try {
                val response = getFiveDayForecast(
                    latitude = lat,
                    longitude = long)
                _forecastData.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.value = e.message
                Log.e("API", "Error fetching forecast data:", e)
            }
        }
    }

    fun getCityData() {
        viewModelScope.launch {
            val cityList = getCities()
            _cityData.value = cityList
        }
    }
}