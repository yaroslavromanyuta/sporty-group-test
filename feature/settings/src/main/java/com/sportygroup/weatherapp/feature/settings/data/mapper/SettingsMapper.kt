package com.sportygroup.weatherapp.feature.settings.data.mapper

import com.sportygroup.weatherapp.feature.settings.data.model.SettingsDataModel
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import javax.inject.Inject

/** Maps between the persisted [SettingsDataModel] and the domain [AppSettings]. */
class SettingsMapper @Inject constructor() {

    fun toDomain(data: SettingsDataModel): AppSettings = AppSettings(
        measurementSystem = when (data.measurementSystem) {
            MEASUREMENT_IMPERIAL -> MeasurementSystem.IMPERIAL
            MEASUREMENT_METRIC -> MeasurementSystem.METRIC
            else -> AppSettings.DEFAULT.measurementSystem
        },
        temperatureUnit = when (data.temperatureUnit) {
            TEMPERATURE_FAHRENHEIT -> TemperatureUnit.FAHRENHEIT
            TEMPERATURE_CELSIUS -> TemperatureUnit.CELSIUS
            else -> AppSettings.DEFAULT.temperatureUnit
        },
        themeMode = when (data.themeMode) {
            THEME_LIGHT -> ThemeMode.LIGHT
            THEME_DARK -> ThemeMode.DARK
            THEME_SYSTEM -> ThemeMode.SYSTEM
            else -> AppSettings.DEFAULT.themeMode
        },
    )

    fun toData(settings: AppSettings): SettingsDataModel = SettingsDataModel(
        measurementSystem = when (settings.measurementSystem) {
            MeasurementSystem.METRIC -> MEASUREMENT_METRIC
            MeasurementSystem.IMPERIAL -> MEASUREMENT_IMPERIAL
        },
        temperatureUnit = when (settings.temperatureUnit) {
            TemperatureUnit.CELSIUS -> TEMPERATURE_CELSIUS
            TemperatureUnit.FAHRENHEIT -> TEMPERATURE_FAHRENHEIT
        },
        themeMode = when (settings.themeMode) {
            ThemeMode.SYSTEM -> THEME_SYSTEM
            ThemeMode.LIGHT -> THEME_LIGHT
            ThemeMode.DARK -> THEME_DARK
        },
    )

    companion object {
        const val MEASUREMENT_METRIC = "metric"
        const val MEASUREMENT_IMPERIAL = "imperial"
        const val TEMPERATURE_CELSIUS = "celsius"
        const val TEMPERATURE_FAHRENHEIT = "fahrenheit"
        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }
}