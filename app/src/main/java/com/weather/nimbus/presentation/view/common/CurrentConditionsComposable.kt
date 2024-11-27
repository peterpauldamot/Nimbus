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
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.WeatherData
import com.weather.nimbus.presentation.theme.DuskyAmethyst
import com.weather.nimbus.presentation.theme.NimbusTheme
import com.weather.nimbus.presentation.theme.RainDropBlue
import kotlin.random.Random

//TODO: Create a composable with summary of pressure, cloudiness, precipitation

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
fun TemperatureBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_temperature)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (mainConditions != null) {
                val clampedTemperature = mainConditions.temperature.toFloat().coerceIn(-50f, 50f)
                val baseRotationAngle = clampedTemperature * (90f / 50f)
                val animatedRotation = remember { Animatable(baseRotationAngle) }

                LaunchedEffect(baseRotationAngle) {
                    while (true) {
                        val randomOffset = Random.nextFloat() * 20f - 10f
                        val targetAngle = baseRotationAngle + randomOffset

                        animatedRotation.animateTo(
                            targetValue = targetAngle,
                            animationSpec = tween(
                                durationMillis = 2000,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.width(200.dp),
                            painter = painterResource(id = R.drawable.current_conditions_temperature_scale),
                            contentDescription = "Weather Scale"
                        )
                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .offset(y = 25.dp)
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
                Text(
                    text = stringResource(R.string.label_common_no_data_on_location),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}

@Composable
fun HumidityBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_humidity)) {
        if (mainConditions != null) {
            val minHumidity = 0
            val maxHumidity = 100

            val clampedHumidity = mainConditions.humidity.coerceIn(minHumidity, maxHumidity)
            val fillHeight = (clampedHumidity / 100f)
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
                            .offset(x = (-8).dp, y = (0).dp)
                            .height(70.dp)
                            .clip(shape = makeDropletShape())
                            .background(
                                color = Color.Transparent
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fillHeight)
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
                        .height(70.dp)
                        .width(10.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_common_arrow_left),
                        contentDescription = "Pressure bar arrow pointer",
                        modifier = Modifier
                            .size(10.dp)
                            .offset(
                                x = (-2).dp,
                                y = -(fillHeight * 70).dp + 10.dp / 2),
                        tint = DuskyAmethyst
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.label_common_no_data_on_location),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
fun WindBox(
    modifier: Modifier,
    windData: WeatherData.Wind?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_wind)) {
        if (windData != null) {
            val baseRotationAngle = windData.degrees.toFloat()
            val animatedRotationAngle = remember { Animatable(initialValue = baseRotationAngle) }
            LaunchedEffect(baseRotationAngle) {
                while (true) {
                    val randomOffset = Random.nextFloat() * 10f - 5f
                    val targetAngle = baseRotationAngle + randomOffset

                    animatedRotationAngle.animateTo(
                        targetValue = targetAngle,
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearEasing
                        )
                    )
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
                        text = "N",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiary
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.icon_current_conditions_direction),
                        contentDescription = "Wind Direction",
                        modifier = Modifier
                            .size(50.dp)
                            .rotate(animatedRotationAngle.value),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.label_common_no_data_on_location),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
fun PressureBox(
    modifier: Modifier,
    mainConditions: WeatherData.Main?
) {
    CurrentConditionsBox(modifier, stringResource(R.string.label_current_conditions_pressure)) {
        if (mainConditions != null) {
            val pressure = 1200
            val pointerMinAngle = 150f
            val pointerMaxAngle = 135f

            val minPressure = 890
            val maxPressure = 1100
            val clampedPressure = pressure.coerceIn(minPressure, maxPressure)
            val baseRotationAngle = pointerMinAngle - ((clampedPressure - minPressure) / (maxPressure - minPressure)) * (pointerMinAngle + pointerMaxAngle)

            val animatedRotation = remember { Animatable(baseRotationAngle) }

            LaunchedEffect(baseRotationAngle) {
                while (true) {
                    val randomOffset = Random.nextFloat() * 10f - 5f
                    val targetAngle = baseRotationAngle + randomOffset
                    animatedRotation.animateTo(
                        targetValue = targetAngle,
                        animationSpec = tween(
                            durationMillis = 100,
                            easing = LinearOutSlowInEasing
                        )
                    )
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
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(id = R.drawable.current_conditions_pressure_scale),
                        contentDescription = "Pressure Scale"
                    )
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = 14.dp)
                            .rotate(animatedRotation.value),
                        painter = painterResource(id = R.drawable.current_conditions_pressure_pointer),
                        contentDescription = "Pressure Pointer"
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.label_common_no_data_on_location),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
fun CurrentConditionsBox(
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
            feelsLike = 286.37,
            minTemperature = 27,
            maxTemperature = 31,
            pressure = 1020,
            humidity = 87
        ),
        wind = WeatherData.Wind(
            speed = 2.06,
            speedClassification = "Light breeze",
            direction = "North",
            degrees = 350
        ),
        clouds = 100
    )
    NimbusTheme(weather = WeatherStatus.CLOUDS) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
            CurrentConditions(weatherData = mockWeatherData)
        }

    }
}