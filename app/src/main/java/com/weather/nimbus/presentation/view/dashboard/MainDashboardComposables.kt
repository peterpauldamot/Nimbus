/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.weather.nimbus.R
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.presentation.theme.AfternoonColor
import com.weather.nimbus.presentation.theme.EveningColor
import com.weather.nimbus.presentation.theme.LateMorningColor
import com.weather.nimbus.presentation.theme.LateNightColor
import com.weather.nimbus.presentation.theme.MidnightColor
import com.weather.nimbus.presentation.theme.MorningColor
import com.weather.nimbus.presentation.theme.NightColor
import com.weather.nimbus.presentation.theme.NimbusTheme
import com.weather.nimbus.presentation.theme.PreDawnColor
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun MainDashboardComposables(weatherViewModel: WeatherViewModel) {
    // Collect the weather data and error states as State objects
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val forecastData by weatherViewModel.forecastData.collectAsState()
    val cityData by weatherViewModel.cityData.collectAsState()
    val errorState by weatherViewModel.errorState.collectAsState()
    var showSearchBar by rememberSaveable { mutableStateOf(false) }

    val onCitySelected: (CitiesResponse) -> Unit = { city ->
        weatherViewModel.getCurrentWeatherOnCity(
            lat = city.coordinates?.latitude,
            long = city.coordinates?.longitude
        )
    }

    NimbusTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.background(
                    getGradientForCurrentTheme(
                        MaterialTheme.colorScheme.background))
            ) {
                    MyTopBar(
                        onSearchIconClick = { showSearchBar = true }
                    )
                    TemperatureHeader(
                        mainTemp = weatherData?.main,
                        weather = weatherData?.weather?.first(),
                        cityName = weatherData?.cityName ?: "Current Location"
                    )
                    Spacer(modifier = Modifier.height(120.dp))

                    Spacer(modifier = Modifier.height(40.dp))

                    FiveDayDailyForecast()
                    Spacer(modifier = Modifier.height(102.dp))

                    FiveDayForecastButton()
            }

            if (showSearchBar) {
                LocationSearchBar(
                    onClose = { showSearchBar = false },
                    onCitySelected = onCitySelected,
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
    onSearchIconClick: () -> Unit
) {
    TopAppBar(
        title = {},
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
    onCitySelected: (city: CitiesResponse) -> Unit,
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
                                    city?.let {
                                        textFieldState.setTextAndPlaceCursorAtEnd(it.name)
                                        onCitySelected(it)
                                        expanded = false
                                        onClose()
                                    }
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
    mainTemp: CurrentWeatherResponse.Main?,
    weather: CurrentWeatherResponse.Weather?,
    cityName: String
) {
    val temperature = convertToCelsius(mainTemp?.temperature)
    val feelsLike = convertToCelsius(mainTemp?.feelsLike)

    val description = capitalizeFirstLetter(weather?.description)
    val maxTemperature = convertToCelsius(mainTemp?.maxTemperature)
    val minTemperature = convertToCelsius(mainTemp?.minTemperature)

    Box (
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MakeWeatherStatusImage("Snowy")
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cityName,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = stringResource(R.string.temperature_degrees_celsius, temperature),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val density = this@layout.density
                        val adjustmentInPx = (32 * density).toInt()
                        val adjustedHeight = placeable.height - adjustmentInPx
                        layout(placeable.width, adjustedHeight) {
                            placeable.placeRelative(0, -(adjustmentInPx)/2)
                        }
                    }
            )

            Text(
                text = "$description",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = stringResource(R.string.temperature_feels_like, feelsLike),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
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

@Preview
@Composable
fun FiveDayDailyForecast() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(5) {
            ForecastBox("Thu", "Sunny", "19° / 26°")
        }
    }
}

@Composable
private fun ForecastBox(
    day: String,
    icon: String,
    minMaxTemp: String
) {
    Box (modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(4.dp))
        .padding(8.dp)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = icon,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = minMaxTemp,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

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

private fun convertToCelsius(kelvin: Double?): String {
    // Convert to Celsius and round it to an integer
    val celsius = kelvin?.minus(273.15)?.roundToInt() ?: 0
    return String.format("%02d", celsius)
}

private fun capitalizeFirstLetter(input: String?): String? {
    if (input == null) return null
    return input
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } } // Capitalize the first letter of each word
}

private fun getGradientForCurrentTheme(backgroundColor: Color): Brush {
    val timeColor = getColorForCurrentTime()

    return Brush.verticalGradient(
        colors = listOf(backgroundColor, timeColor),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
}

private fun getColorForCurrentTime(): Color {
    val currentTime = LocalTime.now()
    val hour = currentTime.hour

    return when (hour) {
        in 6..8 -> MorningColor
        in 9..11 -> LateMorningColor
        in 12..14 -> AfternoonColor
        in 15..17 -> EveningColor
        in 18..20 -> NightColor
        in 21..23 -> LateNightColor
        in 0..2 -> MidnightColor
        else -> PreDawnColor
    }
}
