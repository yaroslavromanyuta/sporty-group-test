package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.feature.forecast.presentation.model.TemperatureUnit
import javax.inject.Inject
import kotlin.math.roundToInt

/** Formats Celsius domain values into unit-aware, display-ready strings. */
class TemperatureFormatter @Inject constructor() {

    /** Rounded integer value in the requested unit, without a degree symbol. */
    fun value(celsius: Double, unit: TemperatureUnit): Int = when (unit) {
        TemperatureUnit.CELSIUS -> celsius.roundToInt()
        TemperatureUnit.FAHRENHEIT -> (celsius * 9 / 5 + 32).roundToInt()
    }

    /** Rounded value with a trailing degree symbol, e.g. "24°". */
    fun degrees(celsius: Double, unit: TemperatureUnit): String = "${value(celsius, unit)}°"
}