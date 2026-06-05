package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.model.WeatherMetricUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData

/** 2×2 grid of secondary weather metrics (feels like, humidity, wind, pressure). */
@Composable
fun WeatherMetricsGrid(
    metrics: List<WeatherMetricUiModel>,
    modifier: Modifier = Modifier,
) {
    SkyCard(modifier = modifier, contentPadding = PaddingValues(18.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            metrics.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    rowItems.forEach { metric ->
                        WeatherMetricItem(metric = metric, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1) Box(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeatherMetricItem(metric: WeatherMetricUiModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = metric.contentDescription },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(SkyTheme.colors.primarySoft, SkyTheme.shapes.metricIcon),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(icon = metric.icon, size = 21.dp, tint = SkyTheme.colors.primary)
        }
        Column {
            Text(
                text = metric.label,
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = metric.value,
                    color = SkyTheme.colors.textHigh,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                )
                if (metric.unit != null) {
                    Text(
                        text = metric.unit,
                        color = SkyTheme.colors.textLow,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        modifier = Modifier.padding(start = 3.dp, bottom = 1.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WeatherMetricsGridPreview() {
    SkyTheme {
        WeatherMetricsGrid(metrics = ForecastPreviewData.metrics, modifier = Modifier.padding(8.dp))
    }
}