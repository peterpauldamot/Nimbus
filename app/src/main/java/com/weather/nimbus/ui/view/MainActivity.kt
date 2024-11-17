/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.location.LocationProviderImpl
import com.weather.nimbus.data.weather.NetworkClient
import com.weather.nimbus.data.weather.source.service.OpenWeatherServiceImpl
import com.weather.nimbus.domain.cityList.CityListRepository
import com.weather.nimbus.domain.cityList.CityListUseCase
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
        initialDataLoad()
        retrieveCityListFromJson()

        enableEdgeToEdge()
        setContent {
            MainDashboard(weatherViewModel = weatherViewModel)
        }
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
                initialDataLoad()
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

        val cityListRepository = CityListRepository(context = this)
        val cityListUseCase = CityListUseCase(cityListRepository)

        val factory = WeatherViewModelFactory(
            openWeatherService,
            locationUseCase,
            cityListUseCase)
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    private fun setupLocationServices() {
        locationHelper = LocationHelper(this)
        locationHelper.requestLocationPermission(this)
    }

    private fun initialDataLoad() {
        weatherViewModel.getCurrentWeather()
    }

    private fun retrieveCityListFromJson() {
        weatherViewModel.loadCityData()
    }
}

@Preview(showBackground = true)
@Composable
fun NimbusPreview() {
    lateinit var weatherViewModel: WeatherViewModel
    MainDashboard(weatherViewModel = weatherViewModel)
}
