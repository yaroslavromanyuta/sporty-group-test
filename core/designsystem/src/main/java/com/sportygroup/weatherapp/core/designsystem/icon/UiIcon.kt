package com.sportygroup.weatherapp.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.R
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/**
 * Outline UI icon set, backed by tintable vector drawables in `:core:designsystem`.
 * The drawables are authored in a single color and recolored at render time via the tint.
 */
enum class UiIconType(@DrawableRes val drawableRes: Int) {
    LOCATION(R.drawable.ic_ui_location),
    LOCATION_FILL(R.drawable.ic_ui_location_fill),
    SEARCH(R.drawable.ic_ui_search),
    CHEVRON(R.drawable.ic_ui_chevron),
    CHEVRON_DOWN(R.drawable.ic_ui_chevron_down),
    BACK(R.drawable.ic_ui_back),
    CLOSE(R.drawable.ic_ui_close),
    REFRESH(R.drawable.ic_ui_refresh),
    WIND(R.drawable.ic_ui_wind),
    HUMIDITY(R.drawable.ic_ui_humidity),
    PRESSURE(R.drawable.ic_ui_pressure),
    THERMO(R.drawable.ic_ui_thermo),
    MOON(R.drawable.ic_ui_moon),
    SUN(R.drawable.ic_ui_sun),
    WARNING(R.drawable.ic_ui_warning),
    CLOCK(R.drawable.ic_ui_clock),
    HISTORY(R.drawable.ic_ui_history),
    EYE(R.drawable.ic_ui_eye),
    SUNRISE(R.drawable.ic_ui_sunrise),
}

@Composable
fun UiIcon(
    icon: UiIconType,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = SkyTheme.colors.textHigh,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(icon.drawableRes),
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}