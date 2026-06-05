package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * SkyCast type scale. The design uses Manrope; to keep the project buildable without
 * bundling font binaries we fall back to the platform sans-serif and reproduce the
 * weights/sizes that define the look (notably the light, oversized hero temperature).
 */
private val SkyFontFamily = FontFamily.SansSerif

val SkyTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-4).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 23.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 19.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 0.6.sp,
    ),
)