/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.presentation.view.dashboard

import android.media.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.nimbus.R
import com.weather.nimbus.presentation.theme.NimbusTheme
import kotlin.random.Random

@Composable
fun MakeWeatherStatusImage(weatherStatus: String?) {
    Box(modifier = Modifier.size(200.dp)) {
        when (weatherStatus?.lowercase()) {
            "clear" -> SunnyWeatherStatus()
            "rain" -> RainyWeatherStatus()
            "thunderstorm" -> WeatherStatusTemplateWithImage(R.drawable.weather_status_thunderstorm)
            "snow" -> WeatherStatusTemplateWithImage(R.drawable.weather_status_snowy)
            "atmosphere" -> WeatherStatusTemplateWithImage(R.drawable.weather_status_windy)
            "clouds" -> WeatherStatusTemplateWithImage(R.drawable.weather_status_foggy)
            else -> SunnyWeatherStatus()
        }
    }
}

@Composable
private fun SunnyWeatherStatus() {
    val sunDrawable = painterResource(id = R.drawable.weather_status_clear_day)
    val rotationAngle by rememberInfiniteTransition(label = "Sun Rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 90000, easing = LinearEasing)
        ),
        label = "rotationAngle"
    )

    Image(
        painter = sunDrawable,
        contentDescription = "Rotating Sun",
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
        val cloudOffset by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Cloud Offset Animation"
        )
        Image(
            painter = painterResource(id = R.drawable.weather_status_raincloud), // Replace with your cloud drawable
            contentDescription = "Cloud",
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
        contentDescription = "Cloud",
        modifier = Modifier
            .size(200.dp)
    )
}

@Composable
private fun RaindropAnimation(delay: Int) {
    val transition = rememberInfiniteTransition()
    val dropXPosition by transition.animateFloat(
        initialValue = -5f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop X Position Animation"
    )

    val dropYPosition by transition.animateFloat(
        initialValue = 40f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop Y Position Animation"
    )

    val dropAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200 + delay
                0f at 500 with LinearEasing
                1f at 1200 with LinearEasing
                0.8f at durationMillis-300 with LinearEasing
                0f at durationMillis
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop Alpha Animation"
    )

    Image(
        painter = painterResource(id = R.drawable.weather_status_raindrop), // Replace with your raindrop drawable
        contentDescription = "Raindrop",
        modifier = Modifier
            .offset(x = dropXPosition.dp, y = dropYPosition.dp) // Diagonal offset applied here
            .alpha(dropAlpha) // Raindrop fades as it falls
            .size(16.dp) // Adjust size as needed
    )
}

@Preview
@Composable
fun WeatherPreview() {
    NimbusTheme {
        Box {
            MakeWeatherStatusImage("foggy")
        }
    }
}