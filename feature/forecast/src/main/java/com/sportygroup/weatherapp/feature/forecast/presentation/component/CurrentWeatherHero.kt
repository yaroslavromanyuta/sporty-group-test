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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.R
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
                .padding(top = SkyTheme.spacing.s)
                .clearAndSetSemantics { contentDescription = current.contentDescription },
        ) {
            Text(
                text = current.temperature,
                color = SkyTheme.colors.textHigh,
                style = SkyTheme.typography.heroTemperature,
            )
            Text(
                text = "°",
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.heroDegree,
                modifier = Modifier.padding(top = SkyTheme.spacing.sm),
            )
        }
        Text(
            text = current.conditionLabel,
            color = SkyTheme.colors.textHigh,
            style = SkyTheme.typography.titleM,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = SkyTheme.spacing.xs),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.ml),
            modifier = Modifier.padding(top = SkyTheme.spacing.s),
        ) {
            Text(
                text = stringResource(R.string.forecast_hero_high, current.highLabel),
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.forecast_hero_low, current.lowLabel),
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.bodyMedium,
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