package com.weather.nimbus.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.nimbus.R
import com.weather.nimbus.data.model.CurrentWeatherResponse
import com.weather.nimbus.ui.theme.NimbusTheme
import com.weather.nimbus.ui.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun MainDashboard(weatherViewModel: WeatherViewModel) {
    // Collect the weather data and error states as State objects
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val errorState by weatherViewModel.errorState.collectAsState()

    NimbusTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                NimbusTheme {
                    MyTopBar(cityName = weatherData?.cityName ?: "Current Location")
                    Spacer(modifier = Modifier.height(16.dp))

                    TemperatureHeader(mainTemp = weatherData?.main)
                    Spacer(modifier = Modifier.height(120.dp))

                    CurrentWeatherImage(
                        weather = weatherData?.weather?.first(),
                        mainTemp = weatherData?.main)
                    Spacer(modifier = Modifier.height(48.dp))

                    OtherDetails(main = weatherData?.main, wind = weatherData?.wind)
                    Spacer(modifier = Modifier.height(102.dp))

                    FiveDayForecastButton()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(cityName: String) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = cityName)
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle navigation icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle navigation icon click */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert  ,
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black
        )
    )
}

@Composable
fun TemperatureHeader(
    mainTemp: CurrentWeatherResponse.Main?
) {
    val temperature = convertToCelsius(mainTemp?.temperature).toString()
    val feelsLike = convertToCelsius(mainTemp?.feelsLike).toString()

    Box {
        Column {
            Text(
                text = stringResource(R.string.temperature_degrees_celsius, temperature),
                fontSize = 48.sp
            )
            Text(text = stringResource(R.string.temperature_feels_like, feelsLike))
        }
    }
}

@Composable
fun CurrentWeatherImage(
    weather: CurrentWeatherResponse.Weather?,
    mainTemp: CurrentWeatherResponse.Main?
) {
    val description = capitalizeFirstLetter(weather?.description)
    val maxTemperature = convertToCelsius(mainTemp?.maxTemperature).toString()
    val minTemperature = convertToCelsius(mainTemp?.minTemperature).toString()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RotatingSun()
            Text(
                text = "$description $maxTemperature°C / ${minTemperature}°C",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OtherDetails(main: CurrentWeatherResponse.Main?,
                 wind: CurrentWeatherResponse.Wind?) {
    val humidity = main?.humidity
    val windSpeed = wind?.speed

    Column {
        Text(
            text = "Chance of Rain: TODO"
        )
        Text(
            text = "Humidity: $humidity%"
        )
        Text(
            text = "Wind Speed: $windSpeed km/h"
        )
    }
}

@Composable
fun FiveDayForecastButton() {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { /* TODO */ }
        ) {
            Text(
                text = "5 DAY FORECAST",
                color = Color.Black,
                fontSize = 24.sp
            )
        }
    }
}

fun convertToCelsius(kelvin: Double?): Int {
    return (kelvin?.minus(273.15)?.roundToInt()) ?: 0
}

fun capitalizeFirstLetter(input: String?): String? {
    if (input == null) return null
    return input
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } } // Capitalize the first letter of each word
}