/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.weather.nimbus.R
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.presentation.theme.NimbusTheme
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun MainDashboard(weatherViewModel: WeatherViewModel) {
    // Collect the weather data and error states as State objects
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val forecastData by weatherViewModel.forecastData.collectAsState()
    val cityData by weatherViewModel.cityData.collectAsState()
    val errorState by weatherViewModel.errorState.collectAsState()
    var showSearchBar by rememberSaveable { mutableStateOf(false) }

    NimbusTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                NimbusTheme {
                    MyTopBar(
                        cityName = weatherData?.cityName ?: "Current Location",
                        onSearchIconClick = { showSearchBar = true }
                    )
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

            if (showSearchBar) {
                LocationSearchBar(
                    onClose = { showSearchBar = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .zIndex(1f) // Ensures this element appears above others
                        .clickable { showSearchBar = false },
                    cityList = cityData
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    cityName: String,
    onSearchIconClick: () -> Unit
) {
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
            IconButton(onClick = onSearchIconClick) {
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
                    imageVector = Icons.Filled.MoreVert,
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LocationSearchBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    cityList: List<CitiesResponse?>
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    state = textFieldState,
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search for a city") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = "Clear search text",
                            modifier = Modifier.clickable {
                                textFieldState.clearText()
                            }
                        )
                    },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            val filteredCities = if (cityList.isNotEmpty() && textFieldState.text.length >= 3) {
                cityList.filter {
                    it?.name?.contains(textFieldState.text, ignoreCase = true) ?: false
                }
            } else {
                emptyList()
            }

            if (filteredCities.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .height(250.dp)
                ) {
                    items(filteredCities) { city ->
                        ListItem(
                            headlineContent = { city?.let { Text(text = it.name, color = Color.Black) } },
                            supportingContent = { city?.let { Text(it.country) } },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.AddLocation,
                                    contentDescription = null
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.White),
                            modifier = Modifier
                                .clickable {
                                    city?.let { textFieldState.setTextAndPlaceCursorAtEnd(it.name) }
                                    expanded = false
                                    onClose()
                                }
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp, vertical = 4.dp
                                )
                        )
                    }
                }
            }
        }
    }
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
