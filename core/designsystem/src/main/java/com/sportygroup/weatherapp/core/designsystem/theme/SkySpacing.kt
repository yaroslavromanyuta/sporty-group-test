package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * SkyCast spacing scale. The single source of truth for paddings, gaps and spacers across
 * the app — UI code should reference these tokens (via `SkyTheme.spacing`) rather than raw
 * `dp` values. Values mirror the design handoff exactly.
 */
@Immutable
data class SkySpacing(
    val none: Dp = 0.dp,
    val hairline: Dp = 1.dp,
    val xxxs: Dp = 2.dp,
    val xxs: Dp = 3.dp,
    val xs: Dp = 4.dp,
    val s: Dp = 6.dp,
    val sm: Dp = 8.dp,
    val smPlus: Dp = 9.dp,
    val md: Dp = 10.dp,
    val mdPlus: Dp = 11.dp,
    val m: Dp = 12.dp,
    val mPlus: Dp = 13.dp,
    val ml: Dp = 14.dp,
    val lg: Dp = 16.dp,
    val lgPlus: Dp = 18.dp,
    val xl: Dp = 20.dp,
    val xlPlus: Dp = 22.dp,
    val xxl: Dp = 24.dp,
    val xxxl: Dp = 28.dp,
    val huge: Dp = 36.dp,
    val giant: Dp = 40.dp,
    val screenHorizontal: Dp = 16.dp,
)

val LocalSkySpacing = staticCompositionLocalOf { SkySpacing() }