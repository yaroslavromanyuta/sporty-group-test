package com.sportygroup.weatherapp.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * SkyCast type scale — the single source of truth for text styles. UI code should reference
 * these tokens (via `SkyTheme.typography`) rather than setting `fontSize`/`fontWeight`
 * inline. The design uses Manrope; we fall back to the platform sans-serif and reproduce the
 * exact weights/sizes. Where a usage needs a different weight than the token's default (e.g.
 * a "today"/selected emphasis), it overrides `fontWeight` on top of the token's `style`.
 */
private val SkyFontFamily = FontFamily.SansSerif

private fun skyStyle(
    weight: FontWeight,
    size: Int,
    letterSpacing: Double = 0.0,
) = TextStyle(
    fontFamily = SkyFontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    letterSpacing = letterSpacing.sp,
)

@Immutable
data class SkyTypography(
    /** 96sp light hero temperature. */
    val heroTemperature: TextStyle = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-4).sp,
    ),
    /** 34sp degree symbol next to the hero temperature. */
    val heroDegree: TextStyle = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
    ),
    /** 23sp extra-bold dialog/error titles. */
    val headline: TextStyle = skyStyle(FontWeight.ExtraBold, 23),
    /** 22sp extra-bold screen title. */
    val titleXl: TextStyle = skyStyle(FontWeight.ExtraBold, 22),
    /** 20sp extra-bold sub-screen title. */
    val titleL: TextStyle = skyStyle(FontWeight.ExtraBold, 20),
    /** 19sp extra-bold (city selector / hero condition uses Bold override). */
    val titleM: TextStyle = skyStyle(FontWeight.ExtraBold, 19),
    /** 18sp extra-bold (empty state title). */
    val titleS: TextStyle = skyStyle(FontWeight.ExtraBold, 18),
    /** 17sp extra-bold metric value. */
    val value: TextStyle = skyStyle(FontWeight.ExtraBold, 17),
    /** 16sp bold body / row titles / buttons. */
    val body: TextStyle = skyStyle(FontWeight.Bold, 16),
    /** 15sp semibold (hero H/L, weekly rows via weight override). */
    val bodyMedium: TextStyle = skyStyle(FontWeight.SemiBold, 15),
    /** 14.5sp medium supporting text. */
    val bodySmall: TextStyle = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.5.sp,
    ),
    /** 14sp semibold inline label. */
    val label: TextStyle = skyStyle(FontWeight.SemiBold, 14),
    /** 13sp bold uppercased section label (letterSpacing baked in). */
    val sectionLabel: TextStyle = skyStyle(FontWeight.Bold, 13, letterSpacing = 0.6),
    /** 13sp bold caption / section action. */
    val caption: TextStyle = skyStyle(FontWeight.Bold, 13),
    /** 12.5sp semibold small caption (metric labels, updated, hourly time). */
    val captionSmall: TextStyle = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.5.sp,
    ),
    /** 12sp bold overline (app version, precipitation). */
    val overline: TextStyle = skyStyle(FontWeight.Bold, 12),
    /** 11.5sp medium footnote. */
    val footnote: TextStyle = TextStyle(
        fontFamily = SkyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.5.sp,
    ),
)

val LocalSkyTypography = staticCompositionLocalOf { SkyTypography() }

/** Material 3 typography used for MaterialTheme defaults; SkyCast text uses [SkyTypography]. */
val SkyMaterialTypography = Typography()