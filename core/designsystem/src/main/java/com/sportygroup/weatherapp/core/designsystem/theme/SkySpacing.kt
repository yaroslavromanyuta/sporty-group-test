package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** 4dp-based spacing scale used across SkyCast screens. */
@Immutable
data class SkySpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 22.dp,
    val xxl: Dp = 28.dp,
    val screenHorizontal: Dp = 16.dp,
)

val LocalSkySpacing = staticCompositionLocalOf { SkySpacing() }