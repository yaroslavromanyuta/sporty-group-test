package com.sportygroup.weatherapp.feature.forecast.presentation.model

import androidx.compose.runtime.Immutable
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType

/** Display-ready forecast for a single city. All values are pre-formatted for Compose. */
@Immutable
data class ForecastUiModel(
    val cityName: String,
    val region: String,
    val isCurrentLocation: Boolean,
    val updatedLabel: String,
    val current: CurrentWeatherUiModel,
    val hourly: List<HourlyForecastUiModel>,
    val daily: List<DailyForecastUiModel>,
    val metrics: List<WeatherMetricUiModel>,
)

@Immutable
data class CurrentWeatherUiModel(
    val temperature: String,
    val conditionLabel: String,
    val highLabel: String,
    val lowLabel: String,
    val weatherType: WeatherType,
    val contentDescription: String,
)

@Immutable
data class HourlyForecastUiModel(
    val timeLabel: String,
    val temperatureLabel: String,
    val weatherType: WeatherType,
    val precipitationProbability: Int,
    val isNow: Boolean,
)

@Immutable
data class DailyForecastUiModel(
    val dayLabel: String,
    val isToday: Boolean,
    val conditionLabel: String,
    val weatherType: WeatherType,
    val highLabel: String,
    val lowLabel: String,
    val precipitationProbability: Int,
    /** 0f..1f position of the low end of the range bar within the week's span. */
    val barStartFraction: Float,
    /** 0f..1f width of the range bar within the week's span. */
    val barWidthFraction: Float,
)

@Immutable
data class WeatherMetricUiModel(
    val icon: UiIconType,
    val label: String,
    val value: String,
    val unit: String? = null,
    val contentDescription: String,
)

/** A selectable city result (search row / recent). Carries coordinates for selection. */
@Immutable
data class CityUiModel(
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean = false,
)