package com.weather.nimbus.ui.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.nimbus.R
import kotlin.random.Random

@Composable
fun RotatingSun() {
    // Load your sun drawable
    val sunDrawable = painterResource(id = R.drawable.weather_sun_art) // Replace with your drawable resource ID

    // Create an infinite rotation animation
    val rotationAngle by rememberInfiniteTransition(label = "Sun Rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 90000, easing = LinearEasing)
        ),
        label = "rotationAngle"
    )

    // Display the rotating image
    Image(
        painter = sunDrawable,
        contentDescription = "Rotating Sun",
        modifier = Modifier
            .size(200.dp) // Set your desired size
            .padding(vertical = 8.dp)
            .rotate(rotationAngle) // Apply the rotation animation
    )
}

@Composable
fun RainyCloudAnimation() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Cloud Image Animation
        val cloudOffset by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Cloud Offset Animation"
        )

        // Display the cloud image with a gentle floating effect
        Image(
            painter = painterResource(id = R.drawable.rain_cloud), // Replace with your cloud drawable
            contentDescription = "Cloud",
            modifier = Modifier
                .size(300.dp)
                .offset(y = cloudOffset.dp)
                .align(alignment = Alignment.TopCenter)
        )

        // Space between the cloud and the starting point for the raindrops
        Spacer(modifier = Modifier.height(16.dp))

        // Raindrop Animations - Distribute across horizontal space below the cloud
        Row(
            modifier = Modifier.width(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly // Space out each raindrop
        ) {
            repeat(5) { i ->
                RaindropAnimation(delay = i * 300) // Staggered delay for each raindrop
            }
        }
    }
}

@Composable
fun RaindropAnimation(delay: Int) {
    val transition = rememberInfiniteTransition()
    val randomDelay = Random.nextInt(0, 500) // Random delay between 0 and 500ms

    // Diagonal falling animation for X and Y positions
    val dropXPosition by transition.animateFloat(
        initialValue = 0f,
        targetValue = -20f, // Move to the left as it falls
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop X Position Animation"
    )
    // Raindrop falling position animation
    val dropYPosition by transition.animateFloat(
        initialValue = -230f,
        targetValue = -200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delay, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop Y Position Animation"
    )

    // Raindrop fading effect with initial hidden state
    val dropAlpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200 + delay // Add delay to initial visibility
                0f at 0 with LinearEasing
                1f at 200 with LinearEasing
                0f at durationMillis // Fade out at the end
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "Raindrop Alpha Animation"
    )

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .offset(x = dropXPosition.dp, y = dropYPosition.dp) // Diagonal offset applied here
            .alpha(dropAlpha), // Raindrop fades as it falls
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp, 16.dp)
                .scale(1f, 2f) // Make raindrop elongated
                .background(Color.Blue, shape = CircleShape)
        )
    }

}

@Preview
@Composable
fun WeatherPreview() {
    RotatingSun()
}