package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.core.model.WeatherCondition

/** Maps WMO weather interpretation codes (Open-Meteo) to domain [WeatherCondition]s. */
object WeatherCodeMapper {
    fun toCondition(code: Int, isNight: Boolean = false): WeatherCondition = when (code) {
        0, 1 -> if (isNight) WeatherCondition.CLEAR_NIGHT else WeatherCondition.CLEAR_DAY
        2 -> WeatherCondition.PARTLY_CLOUDY
        3 -> WeatherCondition.CLOUDY
        45, 48 -> WeatherCondition.FOG
        51, 53, 55, 56, 57 -> WeatherCondition.SHOWERS
        61, 63, 65, 66, 67 -> WeatherCondition.RAIN
        71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOW
        80, 81, 82 -> WeatherCondition.SHOWERS
        95, 96, 99 -> WeatherCondition.THUNDERSTORM
        else -> WeatherCondition.UNKNOWN
    }
}