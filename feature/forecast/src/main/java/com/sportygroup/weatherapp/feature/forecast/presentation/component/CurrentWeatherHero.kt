package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CurrentWeatherUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData

/** Centered hero: large weather glyph, oversized temperature, condition and H/L. */
@Composable
fun CurrentWeatherHero(
    current: CurrentWeatherUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WeatherIcon(
            type = current.weatherType,
            size = 132.dp,
            contentDescription = null,
        )
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(top = 6.dp)
                .clearAndSetSemantics { contentDescription = current.contentDescription },
        ) {
            Text(
                text = current.temperature,
                color = SkyTheme.colors.textHigh,
                fontWeight = FontWeight.Light,
                fontSize = 96.sp,
                letterSpacing = (-4).sp,
            )
            Text(
                text = "°",
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 34.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        Text(
            text = current.conditionLabel,
            color = SkyTheme.colors.textHigh,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(top = 6.dp),
        ) {
            Text(
                text = "H:${current.highLabel}",
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
            Text(
                text = "L:${current.lowLabel}",
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun CurrentWeatherHeroPreview() {
    SkyPreview {
        CurrentWeatherHero(current = ForecastPreviewData.current)
    }
}