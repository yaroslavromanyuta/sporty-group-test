package com.sportygroup.weatherapp.core.model

import java.time.LocalDate
import java.time.LocalDateTime

/** Geographic coordinates of a place. */
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
)

/** A place that can be searched for and have a forecast shown. */
data class City(
    val name: String,
    val region: String,
    val coordinates: Coordinates,
    val isCurrentLocation: Boolean = false,
)

/**
 * Current ("now") conditions. Temperature and wind values are expressed in the units the
 * forecast was requested in (see the forecast request units); pressure is always hPa.
 */
data class CurrentWeather(
    val temperature: Double,
    val apparentTemperature: Double,
    val condition: WeatherCondition,
    val humidityPercent: Int,
    val windSpeed: Double,
    val pressureHpa: Double,
    val high: Double,
    val low: Double,
    val updatedAt: LocalDateTime,
)

/** A single hour in the hourly forecast. */
data class HourlyForecast(
    val time: LocalDateTime,
    val temperature: Double,
    val condition: WeatherCondition,
    val precipitationProbability: Int,
)

/** A single day in the weekly forecast. */
data class DailyForecast(
    val date: LocalDate,
    val condition: WeatherCondition,
    val high: Double,
    val low: Double,
    val precipitationProbability: Int,
)

/** Where a [Forecast] was loaded from. Used to surface an offline/cache indicator in the UI. */
enum class ForecastSource {
    NETWORK,
    CACHE,
}

/** Aggregate forecast for a city. */
data class Forecast(
    val city: City,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    /** Origin of this data. Defaults to [ForecastSource.NETWORK] for freshly fetched forecasts. */
    val source: ForecastSource = ForecastSource.NETWORK,
    /**
     * True when served from cache and older than the freshness window (see ForecastCachePolicy).
     * Only meaningful when [source] is [ForecastSource.CACHE]; lets the UI say "may be outdated".
     */
    val isStale: Boolean = false,
)