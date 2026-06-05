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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    SkyCard(modifier = modifier, contentPadding = PaddingValues(14.dp)) {
        Column {
            days.forEachIndexed { index, day ->
                DailyForecastRow(day)
                if (index < days.lastIndex) {
                    HorizontalDivider(
                        color = SkyTheme.colors.cardBorder,
                        modifier = Modifier.padding(horizontal = 4.dp),
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
            .padding(vertical = 11.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = day.dayLabel,
            color = if (day.isToday) SkyTheme.colors.textHigh else SkyTheme.colors.textMedium,
            fontWeight = if (day.isToday) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.width(42.dp),
        )
        WeatherIcon(type = day.weatherType, size = 30.dp, contentDescription = day.conditionLabel)
        Text(
            text = if (day.precipitationProbability > 25) "${day.precipitationProbability}%" else "",
            color = SkyTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.width(34.dp),
        )
        Text(
            text = day.lowLabel,
            color = SkyTheme.colors.textLow,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.width(34.dp),
        )
        RangeBar(
            startFraction = day.barStartFraction,
            widthFraction = day.barWidthFraction,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = day.highLabel,
            color = SkyTheme.colors.textHigh,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.width(34.dp),
        )
    }
}

@Composable
private fun RangeBar(startFraction: Float, widthFraction: Float, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(SkyTheme.colors.track),
    ) {
        val total = maxWidth
        Box(
            modifier = Modifier
                .offset(x = total * startFraction)
                .width(total * widthFraction)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(SkyTheme.colors.primary, SkyTheme.colors.accent),
                    ),
                ),
        )
    }
}

@Preview
@Composable
private fun WeeklyForecastListPreview() {
    SkyTheme {
        WeeklyForecastList(days = ForecastPreviewData.daily, modifier = Modifier.padding(8.dp))
    }
}