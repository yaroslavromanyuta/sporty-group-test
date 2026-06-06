package com.sportygroup.weatherapp.core.designsystem.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/**
 * Multi-color SkyCast weather glyph, rendered from a vector drawable in
 * `:core:designsystem`. [contentDescription] should describe the condition for
 * accessibility, or be null when a textual label already conveys the state.
 */
@Composable
fun WeatherIcon(
    type: WeatherType,
    modifier: Modifier = Modifier,
    size: Dp = SkyTheme.size.weatherIcon,
    contentDescription: String? = null,
) {
    Image(
        painter = painterResource(type.drawableRes),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
    )
}