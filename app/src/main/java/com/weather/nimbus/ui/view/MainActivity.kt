/*
 * Created by Peter Paul Damot on 2024-11-09.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.weather.nimbus.data.network.NetworkClient
import com.weather.nimbus.data.network.api.OpenWeatherServiceImpl
import com.weather.nimbus.ui.theme.NimbusTheme
import com.weather.nimbus.ui.viewmodel.WeatherViewModel
import com.weather.nimbus.ui.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callCurrentWeather()

        enableEdgeToEdge()
        setContent {
            MainDashboard(weatherViewModel = weatherViewModel)
        }
    }

    private fun callCurrentWeather() {
        val openWeatherApi = NetworkClient.create()
        val openWeatherService = OpenWeatherServiceImpl(openWeatherApi)
        val factory = WeatherViewModelFactory(openWeatherService)
        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        // Trigger data loading
        Log.d("Activity", "onCreate: Triggering getCurrentWeather")
        weatherViewModel.getCurrentWeather(
            "16.41",
            "120.59",
            "2f1894939b86e62241429f38569bef0e")
    }
}



@Preview(showBackground = true)
@Composable
fun NimbusPreview() {
    lateinit var weatherViewModel: WeatherViewModel
    MainDashboard(weatherViewModel = weatherViewModel)
}
