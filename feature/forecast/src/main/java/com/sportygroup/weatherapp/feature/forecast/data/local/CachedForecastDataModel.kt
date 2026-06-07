package com.sportygroup.weatherapp.feature.forecast.data.local

import kotlinx.serialization.Serializable

/**
 * Serializable snapshot of a forecast as stored on disk. Dates/times are kept as ISO strings
 * and the weather condition as its enum name so the model has no platform/JSON-unfriendly types.
 * [cachedAtEpochMillis] is the wall-clock time the entry was written and drives freshness checks.
 */
@Serializable
data class CachedForecastDataModel(
    val city: CachedCityDataModel,
    val current: CachedCurrentWeather,
    val hourly: List<CachedHourlyForecast>,
    val daily: List<CachedDailyForecast>,
    val cachedAtEpochMillis: Long,
)

@Serializable
data class CachedCurrentWeather(
    val temperature: Double,
    val apparentTemperature: Double,
    val condition: String,
    val humidityPercent: Int,
    val windSpeed: Double,
    val pressureHpa: Double,
    val high: Double,
    val low: Double,
    val updatedAt: String,
)

@Serializable
data class CachedHourlyForecast(
    val time: String,
    val temperature: Double,
    val condition: String,
    val precipitationProbability: Int,
)

@Serializable
data class CachedDailyForecast(
    val date: String,
    val condition: String,
    val high: Double,
    val low: Double,
    val precipitationProbability: Int,
)