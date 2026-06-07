package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import java.util.Locale

/**
 * Identity of a cached forecast. Two requests share a cache entry only when they target the
 * same location AND the same unit selection, so changing temperature/measurement units (or
 * switching cities) never serves a forecast in the wrong units.
 */
data class ForecastCacheKey(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val temperatureUnit: String,
    val windSpeedUnit: String,
) {
    /** Stable string usable as a preferences key. Coordinates are rounded to avoid float noise. */
    val value: String
        get() = listOf(
            latitude.round4(),
            longitude.round4(),
            cityName.trim().lowercase(Locale.ROOT),
            temperatureUnit,
            windSpeedUnit,
        ).joinToString(separator = "|")

    companion object {
        fun from(city: City, units: ForecastUnits): ForecastCacheKey = ForecastCacheKey(
            latitude = city.coordinates.latitude,
            longitude = city.coordinates.longitude,
            cityName = city.name,
            temperatureUnit = units.temperatureUnit,
            windSpeedUnit = units.windSpeedUnit,
        )

        private fun Double.round4(): String = String.format(Locale.ROOT, "%.4f", this)
    }
}