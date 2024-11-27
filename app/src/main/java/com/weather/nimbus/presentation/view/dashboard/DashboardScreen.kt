/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.weather.nimbus.R
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.common.source.LoadingOverlay
import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.data.weather.model.ForecastData
import com.weather.nimbus.presentation.theme.AfternoonColor
import com.weather.nimbus.presentation.theme.EveningColor
import com.weather.nimbus.presentation.theme.LateMorningColor
import com.weather.nimbus.presentation.theme.LateNightColor
import com.weather.nimbus.presentation.theme.MidnightColor
import com.weather.nimbus.presentation.theme.MorningColor
import com.weather.nimbus.presentation.theme.NightColor
import com.weather.nimbus.presentation.theme.NimbusTheme
import com.weather.nimbus.presentation.theme.PreDawnColor
import com.weather.nimbus.presentation.view.common.CurrentConditions
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun MainDashboardComposables(
    weatherViewModel: WeatherViewModel,
    onNavigateToSettings: () -> Unit
) {
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val forecastData by weatherViewModel.forecastData.collectAsState()
    val cityData by weatherViewModel.cityData.collectAsState()
    val errorState by weatherViewModel.errorState.collectAsState()
    val isLoading = weatherViewModel.isLoading.collectAsState().value
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val onCitySelected: (CitiesResponse) -> Unit = { city ->
        weatherViewModel.getWeatherData(
            latitude = city.coordinates.latitude,
            longitude = city.coordinates.longitude
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    getGradientForCurrentTheme(
                        MaterialTheme.colorScheme.background
                    )
                )
                .verticalScroll(scrollState)
        ) {
            MyTopBar(
                onSearchIconClick = { showSearchBar = true },
                onNavigateToSettings = onNavigateToSettings
            )
            TemperatureHeader(
                mainTemp = weatherData?.mainConditions,
                weather = weatherData?.weather,
                cityName = weatherData?.cityName ?: "Current Location"
            )
            Spacer(modifier = Modifier.height(160.dp))

            FiveDayDailyForecast(forecastData)
            Spacer(modifier = Modifier.height(20.dp))
            FiveDayForecastButton()

            Spacer(modifier = Modifier.height(60.dp))
            CurrentConditions(weatherData = weatherData)
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (showSearchBar) {
            LocationSearchBar(
                onClose = { showSearchBar = false },
                onCitySelected = onCitySelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .zIndex(1f)
                    .clickable { showSearchBar = false },
                cityList = cityData
            )
        }

        LoadingOverlay(isLoading, MaterialTheme.colorScheme.background)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    onSearchIconClick: () -> Unit,
    onNavigateToSettings: () -> Unit
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
            IconButton(onClick = onNavigateToSettings) {
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
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading Suggestions...")
                }
            }
        }
    }
}

@Composable
fun TemperatureHeader(
    mainTemp: WeatherData.Main?,
    weather: WeatherData.Weather?,
    cityName: String
) {
    val temperature = formatTemperatureString(mainTemp?.temperature)
    val feelsLike = mainTemp?.feelsLike ?: "0"
    val description = capitalizeFirstLetter(weather?.description)

    Box (
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            MakeWeatherStatusImage(weather?.weatherStatus)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$description",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(32.dp))

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
                text = stringResource(R.string.temperature_feels_like, feelsLike),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun FiveDayDailyForecast(
    forecast: ForecastData?
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.five_day_forecast),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            forecast?.weatherForecast?.map { dailyForecast ->
                val minTemperature = dailyForecast.minTemperature
                val maxTemperature = dailyForecast.maxTemperature
                ForecastBox(
                    day = dailyForecast.dayOfWeek,
                    icon = dailyForecast.weatherStatus,
                    minMaxTemp = stringResource(
                        R.string.temperature_min_max,
                        minTemperature,
                        maxTemperature
                    )
                )
            }
        }
    }
}

@Composable
private fun ForecastBox(
    day: String,
    icon: WeatherStatus,
    minMaxTemp: String
) {
    Box (modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(18.dp)
        )
        .size(width = 64.dp, height = 88.dp)
        .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Image(
                modifier = Modifier
                    .size(36.dp)
                    .padding(vertical = 5.dp)
                ,
                painter = painterResource(id = makeWeatherStatusIcon(icon)),
                contentDescription = "Weather Icon"
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

private fun formatTemperatureString(kelvin: Int?): String {
    return String.format("%02d", kelvin) ?: ""
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
        colors = listOf(timeColor, backgroundColor),
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

private fun makeWeatherStatusIcon(weatherStatus: WeatherStatus?): Int {
        return when (weatherStatus) {
            WeatherStatus.THUNDERSTORM -> R.drawable.icon_weather_thunderstorm
            WeatherStatus.DRIZZLE -> R.drawable.icon_weather_rainy
            WeatherStatus.RAIN -> R.drawable.icon_weather_rainy
            WeatherStatus.SNOW -> R.drawable.icon_weather_snowy
            WeatherStatus.ATMOSPHERE -> R.drawable.icon_weather_atmosphere
            WeatherStatus.CLEAR -> R.drawable.icon_weather_sunny
            WeatherStatus.CLOUDS -> R.drawable.icon_weather_cloudy
            else -> R.drawable.icon_weather_sunny
        }
}
