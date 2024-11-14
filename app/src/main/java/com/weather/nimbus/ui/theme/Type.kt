/*
 * Created by Peter Paul Damot on 2024-11-09.
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 2024-11-09.
 */

package com.weather.nimbus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    // Display Text Styles (for large titles)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,  // Large, bold for prominent weather condition titles
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,  // Slightly smaller, still bold for important subheadings
        lineHeight = 32.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,  // Smaller title text, still bold
        lineHeight = 28.sp,
        letterSpacing = (-0.2).sp
    ),

    // Headline Text Styles (For large but less prominent text than Display)
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,  // Large headings or section titles
        lineHeight = 28.sp,
        letterSpacing = (-0.15).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,  // Mid-size headings for additional information
        lineHeight = 24.sp,
        letterSpacing = (-0.1).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,  // Smaller headlines for secondary sections
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Title Text Styles (For section headers or page titles)
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,  // Prominent subheadings
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,  // Section titles, lighter than TitleLarge
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,  // Small titles or labels
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body Text Styles (For general content)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,  // Standard body text size
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,  // Smaller body text for less important content
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,  // Small text for detailed weather info, etc.
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    // Label and Small Text Styles (For labels, metadata, or buttons)
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,  // Label or button text
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,  // For smaller labels, metadata
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,  // Smallest text for metadata or details
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)