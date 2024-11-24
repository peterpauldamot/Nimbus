/*
 * Created by Peter Paul Damot on 2024-11-09.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-09.
 */

package com.weather.nimbus.presentation.theme

import android.os.Build
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.weather.nimbus.presentation.viewmodel.WeatherViewModel
import java.time.LocalTime

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Drizzle Color Theme
private val DrizzleColorScheme = lightColorScheme(
    primary = LightRainBlue, // LightRainBlue
    secondary = PaleYellow, // PaleYellow
    tertiary = MistGray, // MistGray

    // Default Colors to Override
    background = SoftBlueGray, // SoftBlueGray
    surface = PaleGray, // PaleGray
    onPrimary = PureWhite, // Text/icons on primary color
    onSecondary = DarkGray, // DarkGray text/icons on secondary color
    onTertiary = DarkGray, // DarkGray text/icons on tertiary color
    onBackground = DarkGray, // DarkGray text/icons on background
    onSurface = LightGray // LightGray text/icons on surface
)

private val SunnyColorScheme = lightColorScheme(
    primary = SunnyOrange, // Sunny orange for primary color
    secondary = LightSkyBlue, // Light sky blue for secondary color
    tertiary = SoftOrange, // Soft orange for accents

    // Default Colors to Override
    background = LightButteryYellow, // Light buttery yellow background
    surface = Gold, // Gold for surfaces like cards or buttons
    onPrimary = PureWhite, // White text/icons on primary color
    onSecondary = DeepBrown, // Deep brown text/icons on secondary color
    onTertiary = DeepBrown, // Deep brown text/icons on tertiary color
    onBackground = DeepBrown, // Dark text on light background
    onSurface = PureWhite // White text/icons on gold surfaces
)

@Composable
fun NimbusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = SunnyColorScheme,
        typography = Typography,
        content = content
    )
}