package com.sportygroup.weatherapp.feature.forecast.screenshot

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.screenshot.RoborazziScreenshotTest
import com.sportygroup.weatherapp.core.designsystem.screenshot.ThemeVariant
import com.sportygroup.weatherapp.feature.forecast.presentation.CitySearchScreen
import com.sportygroup.weatherapp.feature.forecast.presentation.ForecastScreen
import com.sportygroup.weatherapp.feature.forecast.presentation.component.CityRow
import com.sportygroup.weatherapp.feature.forecast.presentation.component.CurrentWeatherHero
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastErrorContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastLoadingContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.ForecastTopBar
import com.sportygroup.weatherapp.feature.forecast.presentation.component.HourlyForecastRow
import com.sportygroup.weatherapp.feature.forecast.presentation.component.InitialChoiceContent
import com.sportygroup.weatherapp.feature.forecast.presentation.component.WeatherMetricsGrid
import com.sportygroup.weatherapp.feature.forecast.presentation.component.WeeklyForecastList
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorMessage
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiState
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import org.junit.Test

/** Screenshot coverage for the forecast feature: every component and every screen state. */
class ForecastScreenshotTest(variant: ThemeVariant) : RoborazziScreenshotTest(variant) {

    // region Components

    @Test
    fun currentWeatherHero() = snapshot("currentWeatherHero") {
        CurrentWeatherHero(current = ForecastPreviewData.current)
    }

    @Test
    fun forecastTopBar() = snapshot("forecastTopBar") {
        ForecastTopBar(
            cityName = "Malaga",
            isCurrentLocation = true,
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }

    @Test
    fun hourlyForecastRow() = snapshot("hourlyForecastRow") {
        HourlyForecastRow(hours = ForecastPreviewData.hourly, modifier = Modifier.padding(8.dp))
    }

    @Test
    fun weeklyForecastList() = snapshot("weeklyForecastList") {
        WeeklyForecastList(days = ForecastPreviewData.daily, modifier = Modifier.padding(8.dp))
    }

    @Test
    fun weatherMetricsGrid() = snapshot("weatherMetricsGrid") {
        WeatherMetricsGrid(metrics = ForecastPreviewData.metrics, modifier = Modifier.padding(8.dp))
    }

    @Test
    fun cityRow() = snapshot("cityRow") {
        CityRow(city = ForecastPreviewData.searchResults.first(), onClick = {})
    }

    @Test
    fun loadingContent() = snapshot("loadingContent") {
        ForecastLoadingContent()
    }

    @Test
    fun errorContent() = snapshot("errorContent") {
        ForecastErrorContent(
            error = SAMPLE_ERROR,
            canRetry = true,
            canSearchAnotherCity = true,
            onRetry = {},
            onSearch = {},
        )
    }

    @Test
    fun initialChoice() = snapshot("initialChoice") {
        InitialChoiceContent(
            permissionDenied = false,
            permissionPermanentlyDenied = false,
            canSearchManually = true,
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onSearch = {},
        )
    }

    @Test
    fun initialChoicePermanentlyDenied() = snapshot("initialChoicePermanentlyDenied") {
        InitialChoiceContent(
            permissionDenied = true,
            permissionPermanentlyDenied = true,
            canSearchManually = true,
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onSearch = {},
        )
    }

    // endregion

    // region Screens

    @Test
    fun screenContent() = snapshot("screenContent") {
        ForecastScreen(
            state = ForecastUiState.Content(ForecastPreviewData.forecast),
            onAction = {},
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }

    @Test
    fun screenLoading() = snapshot("screenLoading") {
        ForecastScreen(
            state = ForecastUiState.Loading,
            onAction = {},
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }

    @Test
    fun screenInitialChoice() = snapshot("screenInitialChoice") {
        ForecastScreen(
            state = ForecastUiState.InitialChoice(),
            onAction = {},
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }

    @Test
    fun screenError() = snapshot("screenError") {
        ForecastScreen(
            state = ForecastUiState.Error(error = SAMPLE_ERROR),
            onAction = {},
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }

    @Test
    fun citySearchResults() = snapshot("citySearchResults") {
        CitySearchScreen(
            state = CitySearchUiState(query = "Ma", results = ForecastPreviewData.searchResults),
            onAction = {},
            onBack = {},
        )
    }

    @Test
    fun citySearchSuggestions() = snapshot("citySearchSuggestions") {
        CitySearchScreen(
            state = CitySearchUiState(recent = ForecastPreviewData.recent),
            onAction = {},
            onBack = {},
        )
    }

    @Test
    fun citySearchEmpty() = snapshot("citySearchEmpty") {
        CitySearchScreen(
            state = CitySearchUiState(query = "Zzzzz", results = emptyList()),
            onAction = {},
            onBack = {},
        )
    }

    // endregion

    private companion object {
        val SAMPLE_ERROR = ErrorMessage(
            title = "Something went wrong",
            message = "We couldn't reach the weather service. Check your connection and try again.",
            code = "NETWORK_TIMEOUT",
        )
    }
}
