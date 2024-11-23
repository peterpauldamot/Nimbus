/*
 * Created by Peter Paul Damot on 11-09-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.weather.nimbus.domain.location.LocationHelper
import com.weather.nimbus.presentation.view.dashboard.MainDashboard
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainDashboard : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    @Inject
    lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun setupLocationServices() {
        locationHelper.requestLocationPermission(this)
    }

    private fun initialDataLoad() {
        weatherViewModel.getCurrentWeather()
    }

    private fun retrieveCityListFromJson() {
        weatherViewModel.getCityData()
    }
}
