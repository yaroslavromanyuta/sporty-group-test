package com.sportygroup.weatherapp.core.model

/**
 * Platform-independent weather condition. Derived from the Open-Meteo WMO weather code
 * in the data layer; presentation maps it to icons and human-readable labels.
 */
enum class WeatherCondition {
    CLEAR_DAY,
    CLEAR_NIGHT,
    PARTLY_CLOUDY,
    CLOUDY,
    FOG,
    SHOWERS,
    RAIN,
    SNOW,
    THUNDERSTORM,
    UNKNOWN,
}