package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType
import com.sportygroup.weatherapp.core.designsystem.icon.toWeatherType
import com.sportygroup.weatherapp.core.model.WeatherCondition
import javax.inject.Inject

/** Maps a domain [WeatherCondition] to a human-readable label and a visual glyph. */
class WeatherConditionUiMapper @Inject constructor() {

    fun label(condition: WeatherCondition): String = when (condition) {
        WeatherCondition.CLEAR_DAY -> "Sunny"
        WeatherCondition.CLEAR_NIGHT -> "Clear"
        WeatherCondition.PARTLY_CLOUDY -> "Partly cloudy"
        WeatherCondition.CLOUDY -> "Cloudy"
        WeatherCondition.FOG -> "Fog"
        WeatherCondition.SHOWERS -> "Light showers"
        WeatherCondition.RAIN -> "Rain"
        WeatherCondition.SNOW -> "Snow"
        WeatherCondition.THUNDERSTORM -> "Thunderstorm"
        WeatherCondition.UNKNOWN -> "—"
    }

    fun weatherType(condition: WeatherCondition): WeatherType = condition.toWeatherType()
}