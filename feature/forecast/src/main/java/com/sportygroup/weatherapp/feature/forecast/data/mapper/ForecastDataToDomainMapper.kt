package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastDataModel
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.HourlyForecast
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

interface ForecastDataToDomainMapper {
    /** Combines parsed weather data with the resolved [city] into a domain [Forecast]. */
    fun map(model: ForecastDataModel, city: City): Forecast
}

class DefaultForecastDataToDomainMapper @Inject constructor() : ForecastDataToDomainMapper {

    override fun map(model: ForecastDataModel, city: City): Forecast {
        val today = model.daily.firstOrNull()
        val current = model.current
        return Forecast(
            city = city,
            current = CurrentWeather(
                temperatureC = current.temperatureC,
                apparentTemperatureC = current.apparentTemperatureC,
                condition = WeatherCodeMapper.toCondition(current.weatherCode),
                humidityPercent = current.humidityPercent,
                windSpeedKmh = current.windSpeedKmh,
                pressureHpa = current.pressureHpa,
                highC = today?.highC ?: current.temperatureC,
                lowC = today?.lowC ?: current.temperatureC,
                updatedAt = parseDateTime(current.time) ?: LocalDateTime.now(),
            ),
            hourly = model.hourly.mapNotNull { entry ->
                val time = parseDateTime(entry.time) ?: return@mapNotNull null
                HourlyForecast(
                    time = time,
                    temperatureC = entry.temperatureC,
                    condition = WeatherCodeMapper.toCondition(entry.weatherCode),
                    precipitationProbability = entry.precipitationProbability,
                )
            },
            daily = model.daily.mapNotNull { entry ->
                val date = parseDate(entry.date) ?: return@mapNotNull null
                DailyForecast(
                    date = date,
                    condition = WeatherCodeMapper.toCondition(entry.weatherCode),
                    highC = entry.highC,
                    lowC = entry.lowC,
                    precipitationProbability = entry.precipitationProbability,
                )
            },
        )
    }

    private fun parseDateTime(value: String?): LocalDateTime? =
        value?.let { runCatching { LocalDateTime.parse(it) }.getOrNull() }

    private fun parseDate(value: String?): LocalDate? =
        value?.let {
            try {
                LocalDate.parse(it)
            } catch (_: DateTimeParseException) {
                null
            }
        }
}