/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.ui.view

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.location.LocationProviderImpl
import com.weather.nimbus.data.weather.network.NetworkClient
import com.weather.nimbus.data.weather.network.api.OpenWeatherServiceImpl
import com.weather.nimbus.domain.location.LocationHelper
import com.weather.nimbus.domain.location.LocationRepository
import com.weather.nimbus.domain.location.LocationUseCase
import com.weather.nimbus.ui.view.dashboard.MainDashboard
import com.weather.nimbus.ui.viewmodel.WeatherViewModel
import com.weather.nimbus.ui.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDataLayer()
        setupLocationServices()

        enableEdgeToEdge()
        setContent {
            MainDashboard(weatherViewModel = weatherViewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        weatherViewModel.getCurrentWeather()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        ) { isGranted ->
            if (isGranted) {
                weatherViewModel.getCurrentWeather()
            } else {
                Log.d("Location", "Location permission denied")
            }
        }
    }

    private fun setupDataLayer() {
        val openWeatherApi = NetworkClient.create()
        val openWeatherService = OpenWeatherServiceImpl(openWeatherApi)

        val locationProvider = LocationProviderImpl(context = this)
        val locationRepository = LocationRepository(locationProvider)
        val locationUseCase = LocationUseCase(locationRepository)

        val factory = WeatherViewModelFactory(openWeatherService, locationUseCase)
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    private fun setupLocationServices() {
        locationHelper = LocationHelper(this)
        locationHelper.requestLocationPermission(this)
    }

    private fun loadCityListJson() {
        Log.d("CityList", "File exists: ${assets.list("")?.contains("CityList.json")}")
        try {
            val json = applicationContext.assets
                .open("CityList.json")
                .bufferedReader()
                .use { it.readText() }
            weatherViewModel.loadCityData(json)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CityList", "Failed to load city list JSON: ${e.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NimbusPreview() {
    lateinit var weatherViewModel: WeatherViewModel
    MainDashboard(weatherViewModel = weatherViewModel)
}
