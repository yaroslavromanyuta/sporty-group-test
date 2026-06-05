package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/** Entry point to SkyCast theming. Provides Material 3 + SkyCast custom tokens. */
@Composable
fun SkyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val skyColors = if (darkTheme) SkyDarkColors else SkyLightColors

    val materialColors = if (darkTheme) {
        darkColorScheme(
            primary = skyColors.primary,
            onPrimary = skyColors.onPrimary,
            background = skyColors.gradientBottom,
            onBackground = skyColors.textHigh,
            surface = skyColors.card,
            onSurface = skyColors.textHigh,
            surfaceVariant = skyColors.chip,
            error = skyColors.danger,
        )
    } else {
        lightColorScheme(
            primary = skyColors.primary,
            onPrimary = skyColors.onPrimary,
            background = skyColors.gradientBottom,
            onBackground = skyColors.textHigh,
            surface = skyColors.card,
            onSurface = skyColors.textHigh,
            surfaceVariant = skyColors.chip,
            error = skyColors.danger,
        )
    }

    CompositionLocalProvider(
        LocalSkyColors provides skyColors,
        LocalSkySpacing provides SkySpacing(),
        LocalSkyShapes provides SkyShapes(),
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = SkyTypography,
            content = content,
        )
    }
}

/** Convenience accessors for SkyCast tokens, à la `MaterialTheme.colorScheme`. */
object SkyTheme {
    val colors: SkyColors
        @Composable @ReadOnlyComposable get() = LocalSkyColors.current
    val spacing: SkySpacing
        @Composable @ReadOnlyComposable get() = LocalSkySpacing.current
    val shapes: SkyShapes
        @Composable @ReadOnlyComposable get() = LocalSkyShapes.current
}