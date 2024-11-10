/*
 * Created by Peter Paul Damot on 2024-11-09.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-10.
 */

package com.weather.nimbus.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.weather.nimbus.ui.viewmodel.WeatherViewModel
import com.weather.nimbus.ui.viewmodel.WeatherViewModelFactory
import com.weather.nimbus.data.network.NetworkClient
import com.weather.nimbus.data.network.api.OpenWeatherServiceImpl
import com.weather.nimbus.ui.theme.NimbusTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NimbusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val openWeatherService = OpenWeatherServiceImpl(NetworkClient.create())
        val factory = WeatherViewModelFactory(openWeatherService)
        weatherViewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        // Collect weather data from StateFlow
        lifecycleScope.launch {
            weatherViewModel.weatherData.collectLatest { weather ->
                Log.d("Activity", "Weather data collected: $weather")
                weather?.let {
                    // TODO: Update UI with weather data
                    println(weather)
                }
            }
        }

        // Collect error state from StateFlow
        lifecycleScope.launch {
            weatherViewModel.errorState.collectLatest { errorMessage ->
                Log.d("Activity", "Error message collected: $errorMessage")
                errorMessage?.let {
                    // TODO: Show error message
                    println(errorMessage)
                }
            }
        }

        // Trigger data loading
        Log.d("Activity", "onCreate: Triggering getCurrentWeather")
        weatherViewModel.getCurrentWeather(
            "16.41",
            "120.59",
            "2f1894939b86e62241429f38569bef0e")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NimbusTheme {
        Greeting("Android")
    }
}