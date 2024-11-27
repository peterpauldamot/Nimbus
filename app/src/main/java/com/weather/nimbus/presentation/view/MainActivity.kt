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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weather.nimbus.domain.location.LocationHelper
import com.weather.nimbus.presentation.view.dashboard.MainDashboardComposables
import com.weather.nimbus.presentation.view.settings.SettingsScreen
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var locationHelper: LocationHelper
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialDataLoad()

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main_dashboard") {
                composable("main_dashboard") {
                    MainDashboardComposables(
                        weatherViewModel = weatherViewModel,
                        onNavigateToSettings = { navController.navigate("settings")}
                    )
                }
                composable("settings") {
                    SettingsScreen(navController = navController)
                }
            }
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

    private fun initialDataLoad() {
        locationHelper.requestLocationPermission(this)
        weatherViewModel.getWeatherData()
        weatherViewModel.getCityData()
    }
}
