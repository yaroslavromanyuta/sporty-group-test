package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkySectionHeader
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.component.CurrentWeatherHero
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastErrorContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastLoadingContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastTopBar
import com.sportygroup.weatherapp.feature.forecast.presentation.component.HourlyForecastRow
import com.sportygroup.weatherapp.feature.forecast.presentation.component.InitialChoiceContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.WeatherMetricsGrid
import com.sportygroup.weatherapp.feature.forecast.presentation.component.WeeklyForecastList
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState

/** Stateless home screen. Renders initial-choice / loading / error / content states. */
@Composable
fun ForecastScreen(
    state: ForecastUiState,
    onAction: (ForecastUiAction) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SkyTheme.colors.gradientTop, SkyTheme.colors.gradientBottom),
                ),
            ),
    ) {
        when (state) {
            is ForecastUiState.InitialChoice -> InitialChoiceContent(
                permissionDenied = state.permissionDenied,
                canSearchManually = state.canSearchManually,
                onUseCurrentLocation = onUseCurrentLocation,
                onSearch = onOpenSearch,
            )
            ForecastUiState.RequestingPermission -> ForecastLoadingContent()
            ForecastUiState.Loading -> ForecastLoadingContent()
            is ForecastUiState.Error -> ForecastErrorContent(
                error = state.error,
                canRetry = state.canRetry,
                canSearchAnotherCity = state.canSearchAnotherCity,
                onRetry = { onAction(ForecastUiAction.OnRetryClick) },
                onSearch = onOpenSearch,
            )
            is ForecastUiState.Content -> ForecastContent(
                forecast = state.forecast,
                onOpenSearch = onOpenSearch,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}

@Composable
private fun ForecastContent(
    forecast: ForecastUiModel,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 28.dp),
    ) {
        ForecastTopBar(
            cityName = forecast.cityName,
            isCurrentLocation = forecast.isCurrentLocation,
            onOpenSearch = onOpenSearch,
            onOpenSettings = onOpenSettings,
        )
        CurrentWeatherHero(
            current = forecast.current,
            modifier = Modifier.padding(top = 10.dp, bottom = 16.dp),
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            UiIcon(icon = UiIconType.CLOCK, size = 14.dp, tint = SkyTheme.colors.textLow)
            Text(
                text = buildString {
                    append(forecast.updatedLabel)
                    if (forecast.region.isNotBlank()) append(" · ${forecast.region}")
                },
                color = SkyTheme.colors.textLow,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
            )
        }

        SkySectionHeader(title = "Hourly")
        Spacer(Modifier.height(12.dp))
        HourlyForecastRow(hours = forecast.hourly)

        Spacer(Modifier.height(22.dp))
        SkySectionHeader(title = "This week", actionText = "7 days")
        Spacer(Modifier.height(12.dp))
        WeeklyForecastList(days = forecast.daily)

        Spacer(Modifier.height(22.dp))
        SkySectionHeader(title = "Today's details")
        Spacer(Modifier.height(12.dp))
        WeatherMetricsGrid(metrics = forecast.metrics)
    }
}

@Preview(heightDp = 1100)
@Composable
private fun ForecastScreenContentPreview() {
    SkyTheme {
        ForecastScreen(
            state = ForecastUiState.Content(ForecastPreviewData.forecast),
            onAction = {},
            onUseCurrentLocation = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }
}

@Preview
@Composable
private fun ForecastScreenLoadingPreview() {
    SkyTheme {
        ForecastScreen(
            state = ForecastUiState.Loading,
            onAction = {},
            onUseCurrentLocation = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }
}

@Preview
@Composable
private fun ForecastScreenInitialChoicePreview() {
    SkyTheme {
        ForecastScreen(
            state = ForecastUiState.InitialChoice(),
            onAction = {},
            onUseCurrentLocation = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }
}