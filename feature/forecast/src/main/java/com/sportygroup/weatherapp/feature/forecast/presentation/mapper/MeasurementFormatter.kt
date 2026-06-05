package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

/** A formatted metric value with its unit label, e.g. ("12", "km/h"). */
data class FormattedMeasure(val value: String, val unit: String)

/**
 * Formats measurement-system-dependent values. Wind speed already arrives in the
 * requested unit from the API; pressure always arrives in hPa, so it is converted here.
 */
class MeasurementFormatter @Inject constructor() {

    fun wind(windSpeed: Double, system: MeasurementSystem): FormattedMeasure = FormattedMeasure(
        value = windSpeed.roundToInt().toString(),
        unit = if (system == MeasurementSystem.IMPERIAL) "mph" else "km/h",
    )

    fun pressure(pressureHpa: Double, system: MeasurementSystem): FormattedMeasure =
        if (system == MeasurementSystem.IMPERIAL) {
            FormattedMeasure(
                value = String.format(Locale.US, "%.2f", pressureHpa * 0.02953),
                unit = "inHg",
            )
        } else {
            FormattedMeasure(value = pressureHpa.roundToInt().toString(), unit = "hPa")
        }
}