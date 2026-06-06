package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * SkyCast size scale. The single source of truth for component dimensions — icon glyph sizes,
 * control/container sizes, circle diameters, border widths and elevations. UI code references
 * these tokens (via `SkyTheme.size`) instead of raw `dp` values, the same way spacing uses
 * [SkySpacing] and corner radii use [SkyShapes]. Values mirror the design handoff exactly.
 */
@Immutable
data class SkySize(
    val none: Dp = 0.dp,

    // Icon glyph sizes (UiIcon / WeatherIcon `size`).
    val iconXxs: Dp = 14.dp,
    val iconXs: Dp = 15.dp,
    val iconS: Dp = 16.dp,
    val iconSm: Dp = 17.dp,
    val iconMd: Dp = 18.dp,
    val iconMdPlus: Dp = 19.dp,
    val iconLg: Dp = 20.dp,
    val iconLgPlus: Dp = 21.dp,
    val iconXl: Dp = 24.dp,
    val iconXxl: Dp = 30.dp,
    val iconXxxl: Dp = 32.dp,
    val iconHuge: Dp = 56.dp,
    val iconGiant: Dp = 62.dp,
    val iconHero: Dp = 132.dp,
    val weatherIcon: Dp = 64.dp,

    // Controls & containers.
    val rangeBarHeight: Dp = 6.dp,
    val progressIndicator: Dp = 18.dp,
    val clearButton: Dp = 26.dp,
    val tempColumnMin: Dp = 34.dp,
    val iconContainer: Dp = 38.dp,
    val iconContainerLg: Dp = 42.dp,
    val dayColumnMin: Dp = 42.dp,
    val segmentHeight: Dp = 44.dp,
    val searchBarHeight: Dp = 52.dp,
    val buttonHeightSecondary: Dp = 54.dp,
    val buttonHeight: Dp = 56.dp,
    val chipMinWidth: Dp = 62.dp,
    val themeCardMinHeight: Dp = 82.dp,

    // Decorative circles (state illustrations).
    val circleSm: Dp = 76.dp,
    val circleMd: Dp = 106.dp,
    val circleLg: Dp = 130.dp,
    val circleXl: Dp = 150.dp,

    // Borders, strokes & elevation.
    val borderThin: Dp = 1.dp,
    val border: Dp = 1.5.dp,
    val borderThick: Dp = 2.dp,
    val stroke: Dp = 2.5.dp,
    val cardElevation: Dp = 6.dp,
)

val LocalSkySize = staticCompositionLocalOf { SkySize() }
