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

class ForecastDataToDomainMapperImpl @Inject constructor() : ForecastDataToDomainMapper {

    override fun map(model: ForecastDataModel, city: City): Forecast {
        val today = model.daily.firstOrNull()
        val current = model.current
        return Forecast(
            city = city,
            current = CurrentWeather(
                temperature = current.temperature,
                apparentTemperature = current.apparentTemperature,
                condition = WeatherCodeMapper.toCondition(current.weatherCode),
                humidityPercent = current.humidityPercent,
                windSpeed = current.windSpeed,
                pressureHpa = current.pressureHpa,
                high = today?.high ?: current.temperature,
                low = today?.low ?: current.temperature,
                updatedAt = parseDateTime(current.time) ?: LocalDateTime.now(),
            ),
            hourly = model.hourly.mapNotNull { entry ->
                val time = parseDateTime(entry.time) ?: return@mapNotNull null
                HourlyForecast(
                    time = time,
                    temperature = entry.temperature,
                    condition = WeatherCodeMapper.toCondition(entry.weatherCode),
                    precipitationProbability = entry.precipitationProbability,
                )
            },
            daily = model.daily.mapNotNull { entry ->
                val date = parseDate(entry.date) ?: return@mapNotNull null
                DailyForecast(
                    date = date,
                    condition = WeatherCodeMapper.toCondition(entry.weatherCode),
                    high = entry.high,
                    low = entry.low,
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
