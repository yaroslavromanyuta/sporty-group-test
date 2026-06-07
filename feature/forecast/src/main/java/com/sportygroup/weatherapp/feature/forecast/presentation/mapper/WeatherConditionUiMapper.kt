package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.StringResources
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType
import com.sportygroup.weatherapp.core.designsystem.icon.toWeatherType
import com.sportygroup.weatherapp.core.model.WeatherCondition
import com.sportygroup.weatherapp.feature.forecast.R
import javax.inject.Inject

/** Maps a domain [WeatherCondition] to a human-readable label and a visual glyph. */
class WeatherConditionUiMapper @Inject constructor(
    private val stringResources: StringResources,
) {

    fun label(condition: WeatherCondition): String = stringResources.getString(
        when (condition) {
            WeatherCondition.CLEAR_DAY -> R.string.condition_clear_day
            WeatherCondition.CLEAR_NIGHT -> R.string.condition_clear_night
            WeatherCondition.PARTLY_CLOUDY -> R.string.condition_partly_cloudy
            WeatherCondition.CLOUDY -> R.string.condition_cloudy
            WeatherCondition.FOG -> R.string.condition_fog
            WeatherCondition.SHOWERS -> R.string.condition_showers
            WeatherCondition.RAIN -> R.string.condition_rain
            WeatherCondition.SNOW -> R.string.condition_snow
            WeatherCondition.THUNDERSTORM -> R.string.condition_thunderstorm
            WeatherCondition.UNKNOWN -> R.string.condition_unknown
        },
    )

    fun weatherType(condition: WeatherCondition): WeatherType = condition.toWeatherType()
}