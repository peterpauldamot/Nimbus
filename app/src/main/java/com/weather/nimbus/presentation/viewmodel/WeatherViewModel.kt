/*
 * Created by Peter Paul Damot on 11-10-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.presentation.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.data.weather.model.ForecastData
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
    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData: StateFlow<WeatherData?> = _weatherData

    private val _forecastData = MutableStateFlow<ForecastData?>(null)
    val forecastData: StateFlow<ForecastData?> = _forecastData

    private val _cityData = MutableStateFlow<List<CitiesResponse>>(emptyList())
    val cityData: StateFlow<List<CitiesResponse?>> = _cityData

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getWeatherData(latitude: Double? = null, longitude: Double? = null) {
        getCurrentWeather(latitude, longitude)
        getFiveDayForecast(latitude, longitude)
    }

    fun getCityData() {
        executeTask(
            task = {
                Log.d("WeatherViewModel", "Fetching cities...")
                getCities()
            },
            onSuccess = { json -> _cityData.value = json.getOrThrow() },
            onError = { throwable ->
                handleError("Error fetching city data.", throwable)
            }
        )
    }

    private fun getCurrentWeather(lat: Double? = null, long: Double? = null) {
        executeTask(
            task = {
                val location = if (lat != null && long != null) {
                    Location("Searched Location").apply {
                        latitude = lat
                        longitude = long
                    }
                } else {
                    fetchLocation()
                }

                location?.let {
                    getCurrentWeather(it.latitude.toString(), it.longitude.toString())
                }
            },
            onSuccess = { response -> _weatherData.value = response.getOrNull() },
            onError = { throwable ->
                handleError("Error fetching weather data.", throwable)
            }
        )
    }

    private fun getFiveDayForecast(lat: Double? = null, long: Double? = null) {
        executeTask(
            task = {
                val location = if (lat != null && long != null) {
                    Location("Searched Location").apply {
                        latitude = lat
                        longitude = long
                    }
                } else {
                    fetchLocation()
                }

                location?.let {
                    getFiveDayForecast(it.latitude.toString(), it.longitude.toString())
                }
            },
            onSuccess = { response -> _forecastData.value = response.getOrNull() },
            onError = { throwable ->
                handleError("Error fetching forecast data.", throwable)
            }
        )
    }

    private suspend fun fetchLocation(): Location? {
        val location = getCurrentLocation()
        if (location == null) {
            _errorState.value = "Location not found."
            Log.e("WeatherViewModel", "Error fetching location: Location is null")
        }
        return location
    }

    private fun <T> executeTask(
        task: suspend () -> T?,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching { task() }
                .onSuccess { result -> result?.let(onSuccess) }
                .onFailure { throwable -> throwable.let(onError)}
            _isLoading.value = false
        }
    }

    private fun handleError(
        message: String,
        throwable: Throwable?
    ) {
        val error = throwable?.localizedMessage ?: "Unknown Error"
        _errorState.value = "An error occurred: $error"

        Log.e("WeatherViewModel", message)
        throwable?.printStackTrace()
    }
}