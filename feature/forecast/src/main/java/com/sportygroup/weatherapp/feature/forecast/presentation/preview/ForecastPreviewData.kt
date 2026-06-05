package com.sportygroup.weatherapp.feature.forecast.presentation.preview

import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CurrentWeatherUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.DailyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.HourlyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.WeatherMetricUiModel

/** Deterministic fake UI data used by @Preview and screenshot/UI tests. */
object ForecastPreviewData {

    val current = CurrentWeatherUiModel(
        temperature = "24",
        conditionLabel = "Partly cloudy",
        highLabel = "26°",
        lowLabel = "17°",
        weatherType = WeatherType.PARTLY,
        contentDescription = "Partly cloudy, 24°",
    )

    val hourly = listOf(
        HourlyForecastUiModel("Now", "24°", WeatherType.PARTLY, 10, isNow = true),
        HourlyForecastUiModel("12:00", "25°", WeatherType.SUNNY, 0, isNow = false),
        HourlyForecastUiModel("15:00", "26°", WeatherType.PARTLY, 10, isNow = false),
        HourlyForecastUiModel("18:00", "23°", WeatherType.CLOUDY, 20, isNow = false),
        HourlyForecastUiModel("21:00", "20°", WeatherType.CLEAR_NIGHT, 5, isNow = false),
        HourlyForecastUiModel("00:00", "18°", WeatherType.CLEAR_NIGHT, 5, isNow = false),
    )

    val daily = listOf(
        DailyForecastUiModel("Today", true, "Partly cloudy", WeatherType.PARTLY, "26°", "17°", 10, 0.14f, 0.64f),
        DailyForecastUiModel("Tue", false, "Sunny", WeatherType.SUNNY, "28°", "18°", 0, 0.21f, 0.71f),
        DailyForecastUiModel("Wed", false, "Sunny", WeatherType.SUNNY, "29°", "19°", 0, 0.28f, 0.71f),
        DailyForecastUiModel("Thu", false, "Light showers", WeatherType.SHOWERS, "23°", "16°", 60, 0.07f, 0.50f),
        DailyForecastUiModel("Fri", false, "Rain", WeatherType.RAIN, "21°", "15°", 80, 0.0f, 0.43f),
        DailyForecastUiModel("Sat", false, "Cloudy", WeatherType.CLOUDY, "24°", "16°", 30, 0.07f, 0.57f),
        DailyForecastUiModel("Sun", false, "Partly cloudy", WeatherType.PARTLY, "25°", "17°", 15, 0.14f, 0.57f),
    )

    val metrics = listOf(
        WeatherMetricUiModel(UiIconType.THERMO, "Feels like", "26°", null, "Feels like 26 degrees"),
        WeatherMetricUiModel(UiIconType.HUMIDITY, "Humidity", "58", "%", "Humidity 58 percent"),
        WeatherMetricUiModel(UiIconType.WIND, "Wind", "12", "km/h", "Wind 12 kilometers per hour"),
        WeatherMetricUiModel(UiIconType.PRESSURE, "Pressure", "1014", "hPa", "Pressure 1014 hectopascal"),
    )

    val forecast = ForecastUiModel(
        cityName = "Malaga",
        region = "Andalusia, Spain",
        isCurrentLocation = true,
        updatedLabel = "Updated 09:30",
        current = current,
        hourly = hourly,
        daily = daily,
        metrics = metrics,
    )

    val searchResults = listOf(
        CityUiModel("Malaga", "Andalusia, Spain", 36.72, -4.42),
        CityUiModel("Madrid", "Spain", 40.42, -3.70),
        CityUiModel("Manchester", "England, UK", 53.48, -2.24),
        CityUiModel("Marseille", "PACA, France", 43.30, 5.37),
    )

    val recent = listOf(
        CityUiModel("Malaga", "Spain", 36.72, -4.42),
        CityUiModel("Lisbon", "Portugal", 38.72, -9.14),
    )
}