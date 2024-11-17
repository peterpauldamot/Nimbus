/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.cityList.model.CityListResponse
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.FiveDayForecastResponse
import com.weather.nimbus.data.weather.source.service.OpenWeatherService
import com.weather.nimbus.domain.cityList.CityListUseCase
import com.weather.nimbus.domain.location.LocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val openWeatherService: OpenWeatherService,
    private val locationUseCase: LocationUseCase,
    private val cityListUseCase: CityListUseCase
) : ViewModel() {
    private val _weatherData = MutableStateFlow<CurrentWeatherResponse?>(null)
    val weatherData: StateFlow<CurrentWeatherResponse?> = _weatherData

    private val _forecastData = MutableStateFlow<FiveDayForecastResponse?>(null)
    val forecastData: StateFlow<FiveDayForecastResponse?> = _forecastData

    private val _cityData = MutableStateFlow<List<CityListResponse>>(emptyList())
    val cityData: StateFlow<List<CityListResponse?>> = _cityData

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

    fun loadCityData() {
        viewModelScope.launch {
            val cityList = cityListUseCase.getCityList()
            _cityData.value = cityList
        }
    }
}

/*
 * MutableStateFlow holds mutable data for weatherData and errorState.
 * weatherData and errorState are public as StateFlow types so they’re read-only in the UI layer.
 * getCurrentWeather updates _weatherData on successful API calls and _errorState in case of an exception.
 */