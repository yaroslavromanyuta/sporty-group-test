package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.model.HourlyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData

/** Horizontally scrolling hourly forecast as chips (the design's "chips" variant). */
@Composable
fun HourlyForecastRow(
    hours: List<HourlyForecastUiModel>,
    modifier: Modifier = Modifier,
) {
    SkyCard(modifier = modifier, contentPadding = PaddingValues(SkyTheme.spacing.ml)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.sm)) {
            items(hours, key = { it.timeLabel }) { hour ->
                HourlyForecastChip(hour)
            }
        }
    }
}

@Composable
private fun HourlyForecastChip(hour: HourlyForecastUiModel) {
    val background = if (hour.isNow) SkyTheme.colors.primary else SkyTheme.colors.chip
    val timeColor = if (hour.isNow) SkyTheme.colors.onPrimary else SkyTheme.colors.textMedium
    val tempColor = if (hour.isNow) SkyTheme.colors.onPrimary else SkyTheme.colors.textHigh
    Column(
        modifier = Modifier
            .widthIn(min = 62.dp)
            .clip(SkyTheme.shapes.chip)
            .background(background)
            .then(
                if (hour.isNow) Modifier
                else Modifier.border(1.dp, SkyTheme.colors.chipBorder, SkyTheme.shapes.chip),
            )
            .padding(horizontal = SkyTheme.spacing.s, vertical = SkyTheme.spacing.m),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SkyTheme.spacing.smPlus),
    ) {
        Text(
            text = hour.timeLabel,
            color = timeColor,
            style = SkyTheme.typography.captionSmall,
            fontWeight = FontWeight.Bold,
        )
        WeatherIcon(
            type = hour.weatherType,
            size = 32.dp,
            contentDescription = null,
        )
        Text(
            text = hour.temperatureLabel,
            color = tempColor,
            style = SkyTheme.typography.body,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

@ThemePreviews
@Composable
private fun HourlyForecastRowPreview() {
    SkyPreview {
        HourlyForecastRow(hours = ForecastPreviewData.hourly, modifier = Modifier.padding(8.dp))
    }
}