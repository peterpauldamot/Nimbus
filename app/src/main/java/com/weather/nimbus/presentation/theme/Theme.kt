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
import com.weather.nimbus.common.model.WeatherStatus
import com.weather.nimbus.data.weather.model.CurrentWeatherResponse
import com.weather.nimbus.data.weather.model.WeatherData
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

private val ThunderstormColorScheme = lightColorScheme(
    primary = ThunderstormPurple, // ThunderstormPurple
    secondary = LightningYellow, // LightningYellow
    tertiary = RainCloudGray, // RainCloudGray

    // Default Colors to Override
    background = StormyBlueGray, // StormyBlueGray
    surface = CharcoalDarkGray, // CharcoalDarkGray
    onPrimary = PureWhite, // Text/icons on primary color
    onSecondary = DarkShadow, // DarkShadow text/icons on secondary color
    onTertiary = SoftLightGray, // SoftLightGray text/icons on tertiary color
    onBackground = SoftLightGray, // SoftLightGray text/icons on background
    onSurface = SoftLightGray // SoftLightGray text/icons on surface
)

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

// Define Rainy Color Scheme
private val RainyColorScheme = lightColorScheme(
    primary = CoolRainBlue, // Rain accents
    secondary = AmberYellow, // Highlight color
    tertiary = CloudyGray, // Secondary highlights

    // Default Colors to Override
    background = NavyBlue, // Dark rain-themed background
    surface = DarkSlateBlue, // Slightly lighter for surface elements
    onPrimary = LightGray, // Text/icons on primary color
    onSecondary = LightGray, // Text/icons on secondary
    onTertiary = DarkGray, // Text/icons on tertiary color
    onBackground = LightGray, // Text/icons on background
    onSurface = LightGray // Text/icons on surface
)

private val SnowyColorScheme = lightColorScheme(
    primary = IcyBlue, // IcyBlue
    secondary = SnowShadowGray, // SnowShadowGray
    tertiary = FrostyGray, // FrostyGray

    // Default Colors to Override
    background = VeryLightGray, // VeryLightGray
    surface = FrostyGray, // FrostyGray
    onPrimary = PureWhite, // Text/icons on primary color
    onSecondary = CharcoalGray, // CharcoalGray text/icons on secondary color
    onTertiary = MediumGray, // MediumGray text/icons on tertiary color
    onBackground = CharcoalGray, // CharcoalGray text/icons on background
    onSurface = MediumGray // MediumGray text/icons on surface
)

private val AtmosphereColorScheme = lightColorScheme(
    primary = FadedBlue, // FadedBlue
    secondary = CloudyHazeGray, // CloudyHazeGray
    tertiary = PaleMistGray, // PaleMistGray

    // Default Colors to Override
    background = LightGrayishBlue, // LightGrayishBlue
    surface = PaleMistGray, // PaleMistGray
    onPrimary = PureWhite, // Text/icons on primary color
    onSecondary = CharcoalGray, // CharcoalGray text/icons on secondary color
    onTertiary = SoftGrayishBlue, // SoftGrayishBlue text/icons on tertiary color
    onBackground = CharcoalGray, // CharcoalGray text/icons on background
    onSurface = SmokyGray // SmokyGray text/icons on surface
)

private val ClearDayColorScheme = lightColorScheme(
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

private val CloudyColorScheme = lightColorScheme(
    primary = SoftBlueGray, // SoftBlueGray for subtle accents
    secondary = CloudWhite, // CloudWhite for highlights
    tertiary = LightCloudGray, // LightCloudGray to complement the cloudy mood

    // Default Colors to Override
    background = OvercastGray, // OvercastGray for the overall cloudy feel
    surface = LightCloudGray, // LightCloudGray for a softer surface
    onPrimary = CharcoalGray, // CharcoalGray text/icons on primary color
    onSecondary = MediumGray, // MediumGray text/icons on secondary color
    onTertiary = MediumGray, // MediumGray text/icons on tertiary color
    onBackground = CharcoalGray, // CharcoalGray text/icons on background
    onSurface = DarkShadowGray // DarkShadowGray text/icons on surface
)

@Composable
fun NimbusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    weather: WeatherStatus?,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> when (weather) {
            WeatherStatus.THUNDERSTORM -> ThunderstormColorScheme
            WeatherStatus.DRIZZLE -> DrizzleColorScheme
            WeatherStatus.RAIN -> RainyColorScheme
            WeatherStatus.SNOW -> SnowyColorScheme
            WeatherStatus.ATMOSPHERE -> AtmosphereColorScheme
            WeatherStatus.CLEAR -> ClearDayColorScheme
            WeatherStatus.CLOUDS -> CloudyColorScheme
            else -> LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}