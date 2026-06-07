package com.sportygroup.weatherapp.core.designsystem.icon

import androidx.annotation.DrawableRes
import com.sportygroup.weatherapp.core.designsystem.R
import com.sportygroup.weatherapp.core.model.WeatherCondition

/** Visual weather glyph variants, each backed by a vector drawable asset. */
enum class WeatherType(@DrawableRes val drawableRes: Int) {
    SUNNY(R.drawable.ic_weather_sunny),
    PARTLY(R.drawable.ic_weather_partly),
    CLOUDY(R.drawable.ic_weather_cloudy),
    RAIN(R.drawable.ic_weather_rain),
    SHOWERS(R.drawable.ic_weather_showers),
    STORM(R.drawable.ic_weather_storm),
    FOG(R.drawable.ic_weather_fog),
    CLEAR_NIGHT(R.drawable.ic_weather_clearnight),
}

/** Maps a domain condition to the glyph that best represents it. */
fun WeatherCondition.toWeatherType(): WeatherType = when (this) {
    WeatherCondition.CLEAR_DAY -> WeatherType.SUNNY
    WeatherCondition.CLEAR_NIGHT -> WeatherType.CLEAR_NIGHT
    WeatherCondition.PARTLY_CLOUDY -> WeatherType.PARTLY
    WeatherCondition.CLOUDY -> WeatherType.CLOUDY
    WeatherCondition.FOG -> WeatherType.FOG
    WeatherCondition.SHOWERS -> WeatherType.SHOWERS
    WeatherCondition.RAIN -> WeatherType.RAIN
    WeatherCondition.SNOW -> WeatherType.SHOWERS
    WeatherCondition.THUNDERSTORM -> WeatherType.STORM
    WeatherCondition.UNKNOWN -> WeatherType.CLOUDY
}