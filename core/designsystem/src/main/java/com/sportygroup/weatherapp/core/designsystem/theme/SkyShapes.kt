package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/** Corner radii from the design tokens: sheet 28, card 24, chip 18, pill (fully rounded). */
@Immutable
data class SkyShapes(
    val sheet: RoundedCornerShape = RoundedCornerShape(28.dp),
    val card: RoundedCornerShape = RoundedCornerShape(24.dp),
    val chip: RoundedCornerShape = RoundedCornerShape(18.dp),
    val pill: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val row: RoundedCornerShape = RoundedCornerShape(16.dp),
    val metricIcon: RoundedCornerShape = RoundedCornerShape(14.dp),
    val iconContainer: RoundedCornerShape = RoundedCornerShape(12.dp),
    val bar: RoundedCornerShape = RoundedCornerShape(3.dp),
)

val LocalSkyShapes = staticCompositionLocalOf { SkyShapes() }