/*
 * Created by Peter Paul Damot on 11-26-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-26-2024.
 */

package com.weather.nimbus.presentation.view.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.nimbus.R
import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.presentation.theme.DuskyAmethyst
import com.weather.nimbus.presentation.theme.NimbusTheme
import com.weather.nimbus.presentation.theme.RainDropBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

//TODO: Create a composable with summary of pressure, cloudiness, precipitation

// COMMON
private const val RANDOM_OFFSET_RANGE = 10f
private const val RANDOM_OFFSET_HALF_RANGE = 5f

// TEMPERATURE
private const val MIN_TEMPERATURE = -50f
private const val MAX_TEMPERATURE = 50f
private const val TEMPERATURE_IMAGE_SCALE_WIDTH = 200
private const val TEMPERATURE_POINTER_SIZE = 60
private const val TEMPERATURE_POINTER_OFFSET_Y = 25f
private const val TEMPERATURE_POINTER_MAX_ROTATION_ANGLE = 90f
private const val TEMPERATURE_OFFSET_RANGE_MIN = -10f
private const val TEMPERATURE_OFFSET_RANGE_MAX = 10f
private const val TEMPERATURE_ANIMATION_DURATION = 1000

// HUMIDITY
private const val MIN_HUMIDITY = 0
private const val MAX_HUMIDITY = 100
private const val HUMIDITY_ANIMATION_DURATION = 1000
private const val HUMIDITY_OFFSET_RANGE = 0.1f
private const val HUMIDITY_OFFSET_HALF_RANGE = 0.05f

// WIND
private const val WIND_DIRECTION_IMAGE_SIZE = 50
private const val WIND_ANIMATION_DURATION = 200

// PRESSURE
private const val MIN_PRESSURE = 890
private const val MAX_PRESSURE = 1100
private const val PRESSURE_IMAGE_SCALE_SIZE = 80
private const val PRESSURE_POINTER_SIZE = 40
private const val PRESSURE_POINTER_OFFSET_Y = 14
private const val PRESSURE_POINTER_MIN_ROTATION_ANGLE = 150f
private const val PRESSURE_POINTER_MAX_ROTATION_ANGLE = 135f
private const val PRESSURE_ANIMATION_DURATION = 100



@Composable
fun CurrentConditions(weatherData: WeatherData?) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.label_current_conditions_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onTertiary
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TemperatureBox(
                        modifier = Modifier.weight(1f),
                        mainConditions = weatherData?.mainConditions)
                    HumidityBox(
                        modifier = Modifier.weight(1f),
                        mainConditions = weatherData?.mainConditions)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WindBox(
                        modifier = Modifier.weight(1f),
                        windData = weatherData?.wind)
                    PressureBox(
                        modifier = Modifier.weight(1f),
                        mainConditions = weatherData?.mainConditions)
                }
            }
        }
    }
}

@Composable
private fun TemperatureBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_temperature)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (mainConditions != null) {
                val clampedTemperature = mainConditions.temperature.toFloat().coerceIn(
                    MIN_TEMPERATURE, MAX_TEMPERATURE)
                val baseRotationAngle = clampedTemperature * (TEMPERATURE_POINTER_MAX_ROTATION_ANGLE / MAX_TEMPERATURE)
                val animatedRotation = remember { Animatable(baseRotationAngle) }

                LaunchedEffect(baseRotationAngle) {
                    val animationSpec = tween<Float>(
                        durationMillis = TEMPERATURE_ANIMATION_DURATION,
                        easing = LinearOutSlowInEasing
                    )
                    while (isActive) {
                        val randomOffset = (
                                Random.nextFloat() * TEMPERATURE_OFFSET_RANGE_MAX
                                ) - TEMPERATURE_OFFSET_RANGE_MIN
                        val targetAngle = baseRotationAngle + randomOffset
                        animatedRotation.animateTo(targetValue = targetAngle, animationSpec = animationSpec)
                    }
                    delay(1000L)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.width(TEMPERATURE_IMAGE_SCALE_WIDTH.dp),
                            painter = painterResource(id = R.drawable.current_conditions_temperature_scale),
                            contentDescription = "Weather Scale"
                        )
                        Image(
                            modifier = Modifier
                                .size(TEMPERATURE_POINTER_SIZE.dp)
                                .offset(y = TEMPERATURE_POINTER_OFFSET_Y.dp)
                                .rotate(animatedRotation.value),
                            painter = painterResource(id = R.drawable.current_conditions_temperature_pointer),
                            contentDescription = "Weather Pointer"
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(
                                R.string.label_value_degrees,
                                mainConditions.minTemperature
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            text = stringResource(
                                R.string.label_value_degrees,
                                mainConditions.maxTemperature
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            } else {
                MakeNoDataText()
            }
        }
    }
}

@Composable
private fun HumidityBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_humidity)) {
        if (mainConditions != null) {
            val clampedHumidity = mainConditions.humidity.coerceIn(MIN_HUMIDITY, MAX_HUMIDITY)
            val baseFillHeight = (clampedHumidity / MAX_HUMIDITY.toFloat())
            val animatedHeight = remember { Animatable(baseFillHeight) }

            LaunchedEffect(baseFillHeight) {
                val animationSpec = tween<Float>(
                    durationMillis = HUMIDITY_ANIMATION_DURATION,
                    easing = LinearEasing
                )

                while (isActive) {
                    val randomOffset = Random.nextFloat() * HUMIDITY_OFFSET_RANGE - HUMIDITY_OFFSET_HALF_RANGE
                    val targetHeight = baseFillHeight + randomOffset
                    animatedHeight.animateTo(targetHeight, animationSpec)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${mainConditions.humidity}%",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }

                Spacer(Modifier.width(16.dp))

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(id = R.drawable.current_conditions_humidity_scale_background),
                        contentDescription = "Pressure Scale"
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .offset(x = (-8).dp)
                            .height(70.dp)
                            .clip(shape = makeDropletShape())
                            .background(
                                color = Color.Transparent
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(animatedHeight.value)
                                .background(
                                    color = RainDropBlue
                                )
                                .align(Alignment.BottomCenter)
                        )
                    }
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(id = R.drawable.current_conditions_humidity_scale_foreground),
                        contentDescription = "Pressure Scale"
                    )
                }

                Box( // Label Arrow Box
                    modifier = Modifier
                        .height(80.dp)
                        .width(10.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column() {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_common_arrow_left),
                            contentDescription = "Pressure bar arrow pointer",
                            modifier = Modifier
                                .size(10.dp)
                                .offset(
                                    x = (-2).dp
                                ),
                            tint = DuskyAmethyst
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(animatedHeight.value)
                        )
                    }
                }
            }
        } else {
            MakeNoDataText()
        }
    }
}

@Composable
private fun WindBox(
    modifier: Modifier,
    windData: WeatherData.Wind?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_wind)) {
        if (windData != null) {
            val baseRotationAngle = windData.degrees.toFloat()
            val animatedRotationAngle = remember { Animatable(initialValue = baseRotationAngle) }

            LaunchedEffect(baseRotationAngle) {
                val animationSpec = tween<Float>(
                    durationMillis = WIND_ANIMATION_DURATION,
                    easing = LinearEasing
                )
                while (isActive) {
                    val randomOffset = Random.nextFloat() * RANDOM_OFFSET_RANGE - RANDOM_OFFSET_HALF_RANGE
                    val targetAngle = baseRotationAngle + randomOffset
                    animatedRotationAngle.animateTo(targetValue = targetAngle, animationSpec = animationSpec)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            )  {
                Column(
                    modifier = Modifier.width(90.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.label_value_with_units_meters_per_second,
                            windData.speed
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Text(
                        text = stringResource(
                            R.string.label_current_conditions_wind_speed_direction,
                            windData.speedClassification,
                            windData.direction
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier.height(90.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.labels_current_conditions_direction_north),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiary
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.icon_current_conditions_direction),
                        contentDescription = "Wind Direction",
                        modifier = Modifier
                            .size(WIND_DIRECTION_IMAGE_SIZE.dp)
                            .rotate(animatedRotationAngle.value),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        } else {
            MakeNoDataText()
        }
    }
}

@Composable
private fun PressureBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_pressure)) {
        if (mainConditions != null) {
            val clampedPressure = mainConditions.pressure.coerceIn(MIN_PRESSURE, MAX_PRESSURE)
            val baseRotationAngle = PRESSURE_POINTER_MIN_ROTATION_ANGLE - (
                    (clampedPressure - MIN_PRESSURE) / (MAX_PRESSURE - MIN_PRESSURE)
                    ) * (PRESSURE_POINTER_MIN_ROTATION_ANGLE + PRESSURE_POINTER_MAX_ROTATION_ANGLE)

            val animatedRotation = remember { Animatable(baseRotationAngle) }

            LaunchedEffect(baseRotationAngle) {
                val animationSpec = tween<Float>(
                    durationMillis = PRESSURE_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
                while (isActive) {
                    val randomOffset = Random.nextFloat() * RANDOM_OFFSET_RANGE - RANDOM_OFFSET_HALF_RANGE
                    val targetAngle = baseRotationAngle + randomOffset
                    animatedRotation.animateTo(targetValue = targetAngle, animationSpec = animationSpec)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${mainConditions.pressure} hpa",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(PRESSURE_IMAGE_SCALE_SIZE.dp),
                        painter = painterResource(id = R.drawable.current_conditions_pressure_scale),
                        contentDescription = "Pressure Scale"
                    )
                    Image(
                        modifier = Modifier
                            .size(PRESSURE_POINTER_SIZE.dp)
                            .offset(y = PRESSURE_POINTER_OFFSET_Y.dp)
                            .rotate(animatedRotation.value),
                        painter = painterResource(id = R.drawable.current_conditions_pressure_pointer),
                        contentDescription = "Pressure Pointer"
                    )
                }
            }
        } else {
            MakeNoDataText()
        }
    }
}

@Composable
private fun CurrentConditionsBox(
    modifier: Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier
        .aspectRatio(ratio = 1f)
        .background(
            color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp, vertical = 12.dp
                )
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
private fun MakeNoDataText() {
    Text(
        text = stringResource(R.string.label_common_no_data_on_location),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onTertiary
    )
}

private fun makeDropletShape(): GenericShape {
    val dropletShape = GenericShape { size, _ ->
        // Start at the bottom center
        moveTo(size.width / 2, size.height)

        // Left curve (starting from bottom to top)
        cubicTo(
            size.width * 0.25f, size.height,  // Control point on left
            0f, size.height * 0.5f,          // Control point at the upper left
            size.width / 2, 0f               // Top center point (pointed tip)
        )

        // Right curve (starting from bottom to top)
        cubicTo(
            size.width, size.height * 0.5f,   // Control point on right
            size.width * 0.75f, size.height,  // Control point at the upper right
            size.width / 2, size.height       // Bottom center point
        )

        close()
    }

    return dropletShape
}

@Preview
@Composable
private fun CurrentConditionsPreview() {
    val mockWeatherData =  WeatherData(
        cityName = "Mountain View",
        date = 1732653653,
        timezone = -28800,
        weather = WeatherData.Weather(
            weatherStatus = WeatherStatus.CLOUDS,
            description = "overcast clouds"
        ),
        mainConditions = WeatherData.Main(
            temperature = 29,
            feelsLike = 27,
            minTemperature = 27,
            maxTemperature = 31,
            pressure = 1020,
            humidity = 50
        ),
        wind = WeatherData.Wind(
            speed = 2.06,
            speedClassification = "Light breeze",
            direction = "North",
            degrees = 350
        ),
        clouds = 100
    )
    NimbusTheme(
        weather = WeatherStatus.CLOUDS,
        selectedTheme = AppThemes.RAINY
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
            CurrentConditions(weatherData = mockWeatherData)
        }

    }
}