package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import javax.inject.Inject

/** Maps user [AppSettings] to Open-Meteo forecast request unit parameters. */
class ForecastUnitsMapper @Inject constructor() {

    fun map(settings: AppSettings): ForecastUnits = ForecastUnits(
        temperatureUnit = when (settings.temperatureUnit) {
            TemperatureUnit.CELSIUS -> "celsius"
            TemperatureUnit.FAHRENHEIT -> "fahrenheit"
        },
        windSpeedUnit = when (settings.measurementSystem) {
            MeasurementSystem.METRIC -> "kmh"
            MeasurementSystem.IMPERIAL -> "mph"
        },
        precipitationUnit = when (settings.measurementSystem) {
            MeasurementSystem.METRIC -> "mm"
            MeasurementSystem.IMPERIAL -> "inch"
        },
    )
}