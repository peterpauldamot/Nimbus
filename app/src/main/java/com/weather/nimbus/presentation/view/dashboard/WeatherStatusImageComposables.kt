/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view.dashboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.nimbus.R
import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.presentation.theme.NimbusTheme
import kotlin.random.Random

@Composable
fun MakeWeatherStatusImage(weatherStatus: WeatherStatus?) {
    Box(modifier = Modifier.size(200.dp)) {
        when (weatherStatus) {
            WeatherStatus.THUNDERSTORM -> WeatherStatusTemplateWithImage(R.drawable.weather_status_thunderstorm)
            WeatherStatus.DRIZZLE -> RainyWeatherStatus()
            WeatherStatus.RAIN -> RainyWeatherStatus()
            WeatherStatus.SNOW -> WeatherStatusTemplateWithImage(R.drawable.weather_status_snowy)
            WeatherStatus.ATMOSPHERE -> WeatherStatusTemplateWithImage(R.drawable.weather_status_windy)
            WeatherStatus.CLEAR -> SunnyWeatherStatus()
            WeatherStatus.CLOUDS -> WeatherStatusTemplateWithImage(R.drawable.weather_status_foggy)
            else -> SunnyWeatherStatus()
        }
    }
}

@Composable
private fun SunnyWeatherStatus() {
    val sunDrawable = painterResource(id = R.drawable.weather_status_clear_day)
    val rotationAngle by rememberInfiniteTransition(
        label = stringResource(R.string.transition_sun_rotation)
    ).animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 90000, easing = LinearEasing)
        ),
        label = stringResource(R.string.animation_sun_rotation_angle)
    )

    Image(
        painter = sunDrawable,
        contentDescription = stringResource(R.string.content_description_rotating_sun),
        modifier = Modifier
            .size(200.dp)
            .rotate(rotationAngle)
    )
}

@Composable
private fun RainyWeatherStatus() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val cloudOffset by rememberInfiniteTransition(
            label = stringResource(R.string.transition_cloud_offset)).animateFloat(
            initialValue = 0f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = stringResource(R.string.animation_cloud_offset)
        )
        Image(
            painter = painterResource(id = R.drawable.weather_status_raincloud),
            contentDescription = stringResource(R.string.content_description_rainy_cloud_image),
            modifier = Modifier
                .size(width = 200.dp, height = 160.dp)
                .offset(y = cloudOffset.dp)
        )
        Row(
            modifier = Modifier.width(180.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(5) {
                val randomDelay = Random.nextInt(0, 1000)
                RaindropAnimation(delay = randomDelay)
            }
        }
    }
}

@Composable
private fun WeatherStatusTemplateWithImage(image: Int) {
    Image(
        painter = painterResource(id = image),
        contentDescription = stringResource(R.string.content_description_generic_weather_status_image),
        modifier = Modifier
            .size(200.dp)
    )
}

@Composable
private fun RaindropAnimation(delay: Int) {
    val transition = rememberInfiniteTransition(label = stringResource(R.string.transition_raindrop))
    val dropXPosition by transition.animateFloat(
        initialValue = -5f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.animation_raindrop_x_position)
    )

    val dropYPosition by transition.animateFloat(
        initialValue = 40f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.animation_raindrop_y_position)
    )

    val dropAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200 + delay
                0f at 500 using LinearEasing
                1f at 1200 using LinearEasing
                0.8f at durationMillis-300 using LinearEasing
                0f at durationMillis
            },
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.animation_raindrop_alpha)
    )

    Image(
        painter = painterResource(id = R.drawable.weather_status_raindrop),
        contentDescription = stringResource(R.string.content_description_raindrop_image),
        modifier = Modifier
            .offset(x = dropXPosition.dp, y = dropYPosition.dp)
            .alpha(dropAlpha)
            .size(16.dp)
    )
}

@Preview
@Composable
fun WeatherPreview() {
    NimbusTheme (
        weather = null,
        selectedTheme = AppThemes.CLEAR
    ) {
        Box {
            MakeWeatherStatusImage(WeatherStatus.CLEAR)
        }
    }
}