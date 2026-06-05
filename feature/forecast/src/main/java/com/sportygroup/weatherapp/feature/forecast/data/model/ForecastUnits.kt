package com.sportygroup.weatherapp.feature.forecast.data.model

/** Open-Meteo unit query parameters derived from the user's settings. */
data class ForecastUnits(
    val temperatureUnit: String,
    val windSpeedUnit: String,
    val precipitationUnit: String,
) {
    companion object {
        val DEFAULT = ForecastUnits(
            temperatureUnit = "celsius",
            windSpeedUnit = "kmh",
            precipitationUnit = "mm",
        )
    }
}