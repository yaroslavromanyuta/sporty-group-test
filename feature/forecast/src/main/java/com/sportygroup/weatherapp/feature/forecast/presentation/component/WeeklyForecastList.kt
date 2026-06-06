package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.model.DailyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData

/** 7-day forecast card with a temperature-range bar per row. */
@Composable
fun WeeklyForecastList(
    days: List<DailyForecastUiModel>,
    modifier: Modifier = Modifier,
) {
    SkyCard(modifier = modifier, contentPadding = PaddingValues(SkyTheme.spacing.ml)) {
        Column {
            days.forEachIndexed { index, day ->
                DailyForecastRow(day)
                if (index < days.lastIndex) {
                    HorizontalDivider(
                        color = SkyTheme.colors.cardBorder,
                        modifier = Modifier.padding(horizontal = SkyTheme.spacing.xs),
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyForecastRow(day: DailyForecastUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SkyTheme.spacing.mdPlus, horizontal = SkyTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.m),
    ) {
        Text(
            text = day.dayLabel,
            color = if (day.isToday) SkyTheme.colors.textHigh else SkyTheme.colors.textMedium,
            style = SkyTheme.typography.bodyMedium,
            fontWeight = if (day.isToday) FontWeight.ExtraBold else FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.widthIn(min = SkyTheme.size.dayColumnMin),
        )
        WeatherIcon(type = day.weatherType, size = SkyTheme.size.iconXxl, contentDescription = day.conditionLabel)
        Text(
            text = if (day.precipitationProbability > 25) "${day.precipitationProbability}%" else "",
            color = SkyTheme.colors.primary,
            style = SkyTheme.typography.overline,
            maxLines = 1,
            modifier = Modifier.widthIn(min = SkyTheme.size.tempColumnMin),
        )
        Text(
            text = day.lowLabel,
            color = SkyTheme.colors.textLow,
            style = SkyTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier.widthIn(min = SkyTheme.size.tempColumnMin),
        )
        RangeBar(
            startFraction = day.barStartFraction,
            widthFraction = day.barWidthFraction,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = day.highLabel,
            color = SkyTheme.colors.textHigh,
            style = SkyTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier.widthIn(min = SkyTheme.size.tempColumnMin),
        )
    }
}

@Composable
private fun RangeBar(startFraction: Float, widthFraction: Float, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .height(SkyTheme.size.rangeBarHeight)
            .clip(SkyTheme.shapes.bar)
            .background(SkyTheme.colors.track),
    ) {
        val total = maxWidth
        Box(
            modifier = Modifier
                .offset(x = total * startFraction)
                .width(total * widthFraction)
                .height(SkyTheme.size.rangeBarHeight)
                .clip(SkyTheme.shapes.bar)
                .background(
                    Brush.horizontalGradient(
                        listOf(SkyTheme.colors.primary, SkyTheme.colors.accent),
                    ),
                ),
        )
    }
}

@ThemePreviews
@Composable
private fun WeeklyForecastListPreview() {
    SkyPreview {
        WeeklyForecastList(days = ForecastPreviewData.daily, modifier = Modifier.padding(8.dp))
    }
}