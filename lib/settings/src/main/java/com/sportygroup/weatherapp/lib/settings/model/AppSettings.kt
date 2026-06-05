package com.sportygroup.weatherapp.lib.settings.model

/** Measurement system for wind speed, pressure and visibility. */
enum class MeasurementSystem {
    METRIC,
    IMPERIAL,
}

/** Temperature unit shown across the app and requested from the weather API. */
enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT,
}

/** App theme preference. */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

/** User-configurable application settings. Public contract shared across modules. */
data class AppSettings(
    val measurementSystem: MeasurementSystem = MeasurementSystem.METRIC,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
) {
    companion object {
        val DEFAULT = AppSettings()
    }
}