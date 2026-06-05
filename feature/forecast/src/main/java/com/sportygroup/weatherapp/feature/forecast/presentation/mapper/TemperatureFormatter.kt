package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Formats already-converted temperature values for display. The weather API returns
 * temperatures in the user-selected unit, so this only rounds and decorates them.
 */
class TemperatureFormatter @Inject constructor() {

    /** Rounded integer value, without a degree symbol (e.g. "24"). */
    fun value(temperature: Double): String = temperature.roundToInt().toString()

    /** Rounded value with a trailing degree symbol (e.g. "24°"). */
    fun degrees(temperature: Double): String = "${temperature.roundToInt()}°"
}