/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.weather.model.cities.CityListModel
import com.weather.nimbus.data.weather.model.weather.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.weather.FiveDayForecastResponse
import com.weather.nimbus.data.weather.network.api.OpenWeatherService
import com.weather.nimbus.domain.location.LocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WeatherViewModel(
    private val openWeatherService: OpenWeatherService,
    private val locationUseCase: LocationUseCase
) : ViewModel() {
    private val _weatherData = MutableStateFlow<CurrentWeatherResponse?>(null)
    val weatherData: StateFlow<CurrentWeatherResponse?> = _weatherData

    private val _forecastData = MutableStateFlow<FiveDayForecastResponse?>(null)
    val forecastData: StateFlow<FiveDayForecastResponse?> = _forecastData

    private val _cityData = MutableStateFlow<CityListModel?>(null)
    val cityData: StateFlow<CityListModel?> = _cityData

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getCurrentWeather() {
        viewModelScope.launch {
            try {
                Log.d("API", "Retrieving Location")
                val location = locationUseCase.getCurrentLocation()
                location?.let {
                    Log.d("API", "Calling getCurrentWeather")
                    val response = openWeatherService.getCurrentWeather(
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
                val response = openWeatherService.getFiveDayForecast(
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

    fun loadCityData(jsonString: String) {
        viewModelScope.launch {
            try {
                val cityList = Json.decodeFromString<CityListModel>(jsonString)
                _cityData.value = cityList
            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.value = e.message
                Log.e("API", "Error loading city list", e)
            }
        }
    }
}

/*
 * MutableStateFlow holds mutable data for weatherData and errorState.
 * weatherData and errorState are public as StateFlow types so they’re read-only in the UI layer.
 * getCurrentWeather updates _weatherData on successful API calls and _errorState in case of an exception.
 */