package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic SkyCast color tokens (mirrors the design handoff `getTheme()` token set).
 * These supplement the Material 3 [androidx.compose.material3.ColorScheme] for the
 * weather-specific surfaces, chips, gradient and cloud glyph colors.
 */
@Immutable
data class SkyColors(
    val primary: Color,
    val primaryPressed: Color,
    val primarySoft: Color,
    val onPrimary: Color,
    val accent: Color,
    val gradientTop: Color,
    val gradientBottom: Color,
    val textHigh: Color,
    val textMedium: Color,
    val textLow: Color,
    val card: Color,
    val cardBorder: Color,
    val chip: Color,
    val chipBorder: Color,
    val danger: Color,
    val dangerSoft: Color,
    val cloud: Color,
    val cloudLine: Color,
    val track: Color,
    val isDark: Boolean,
)

val SkyLightColors = SkyColors(
    primary = Color(0xFF2F6FD0),
    primaryPressed = Color(0xFF255CB0),
    primarySoft = Color(0xFFE7F0FC),
    onPrimary = Color(0xFFFFFFFF),
    accent = Color(0xFFF3B13C),
    gradientTop = Color(0xFFCFE6FB),
    gradientBottom = Color(0xFFF3F8FD),
    textHigh = Color(0xFF16233B),
    textMedium = Color(0xFF5D6C85),
    textLow = Color(0xFF93A1B8),
    card = Color(0xFFFFFFFF),
    cardBorder = Color(0x0F142850),
    chip = Color(0xFFEEF4FC),
    chipBorder = Color(0x0D142850),
    danger = Color(0xFFD6564F),
    dangerSoft = Color(0xFFFDECEB),
    cloud = Color(0xFFCFE3F5),
    cloudLine = Color(0xFF84A6CC),
    track = Color(0x12142850),
    isDark = false,
)

val SkyDarkColors = SkyColors(
    primary = Color(0xFF6AA6F2),
    primaryPressed = Color(0xFF5B97E6),
    primarySoft = Color(0x296AA6F2),
    onPrimary = Color(0xFF0A1322),
    accent = Color(0xFFF6C560),
    gradientTop = Color(0xFF10213C),
    gradientBottom = Color(0xFF0A1322),
    textHigh = Color(0xFFEAF1FB),
    textMedium = Color(0xFFA4B4CD),
    textLow = Color(0xFF6F8099),
    card = Color(0xFF16233B),
    cardBorder = Color(0x14FFFFFF),
    chip = Color(0x10FFFFFF),
    chipBorder = Color(0x12FFFFFF),
    danger = Color(0xFFF08A8A),
    dangerSoft = Color(0x24F08A8A),
    cloud = Color(0xFFC6D6EC),
    cloudLine = Color(0xFF8AA3C6),
    track = Color(0x14FFFFFF),
    isDark = true,
)

val LocalSkyColors = staticCompositionLocalOf { SkyLightColors }