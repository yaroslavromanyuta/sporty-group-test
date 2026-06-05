package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CurrentWeatherUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.DailyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.HourlyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.TemperatureUnit
import com.sportygroup.weatherapp.feature.forecast.presentation.model.WeatherMetricUiModel
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

interface ForecastDomainToUiMapper {
    fun map(forecast: Forecast, unit: TemperatureUnit): ForecastUiModel
}

class DefaultForecastDomainToUiMapper @Inject constructor(
    private val temperatureFormatter: TemperatureFormatter,
    private val conditionMapper: WeatherConditionUiMapper,
    private val dateTimeProvider: DateTimeProvider,
) : ForecastDomainToUiMapper {

    override fun map(forecast: Forecast, unit: TemperatureUnit): ForecastUiModel {
        val current = forecast.current
        return ForecastUiModel(
            cityName = forecast.city.name,
            region = forecast.city.region,
            isCurrentLocation = forecast.city.isCurrentLocation,
            updatedLabel = "Updated %02d:%02d".format(current.updatedAt.hour, current.updatedAt.minute),
            current = CurrentWeatherUiModel(
                temperature = temperatureFormatter.value(current.temperatureC, unit).toString(),
                conditionLabel = conditionMapper.label(current.condition),
                highLabel = temperatureFormatter.degrees(current.highC, unit),
                lowLabel = temperatureFormatter.degrees(current.lowC, unit),
                weatherType = conditionMapper.weatherType(current.condition),
                contentDescription = "${conditionMapper.label(current.condition)}, " +
                    "${temperatureFormatter.degrees(current.temperatureC, unit)}",
            ),
            hourly = mapHourly(forecast, unit),
            daily = mapDaily(forecast, unit),
            metrics = mapMetrics(forecast, unit),
        )
    }

    private fun mapHourly(forecast: Forecast, unit: TemperatureUnit): List<HourlyForecastUiModel> {
        val currentHour = dateTimeProvider.now().truncatedTo(ChronoUnit.HOURS)
        val upcoming = forecast.hourly.filter { !it.time.isBefore(currentHour) }
            .ifEmpty { forecast.hourly }
            .take(24)
        return upcoming.mapIndexed { index, hour ->
            HourlyForecastUiModel(
                timeLabel = if (index == 0) "Now" else "%02d:00".format(hour.time.hour),
                temperatureLabel = temperatureFormatter.degrees(hour.temperatureC, unit),
                weatherType = conditionMapper.weatherType(hour.condition),
                precipitationProbability = hour.precipitationProbability,
                isNow = index == 0,
            )
        }
    }

    private fun mapDaily(forecast: Forecast, unit: TemperatureUnit): List<DailyForecastUiModel> {
        val days = forecast.daily
        if (days.isEmpty()) return emptyList()
        val minLow = days.minOf { it.lowC }
        val maxHigh = days.maxOf { it.highC }
        val span = (maxHigh - minLow).takeIf { it != 0.0 } ?: 1.0
        val today = dateTimeProvider.now().toLocalDate()
        return days.map { day ->
            val isToday = day.date == today
            DailyForecastUiModel(
                dayLabel = if (isToday) "Today" else day.shortDayLabel(),
                isToday = isToday,
                conditionLabel = conditionMapper.label(day.condition),
                weatherType = conditionMapper.weatherType(day.condition),
                highLabel = temperatureFormatter.degrees(day.highC, unit),
                lowLabel = temperatureFormatter.degrees(day.lowC, unit),
                precipitationProbability = day.precipitationProbability,
                barStartFraction = ((day.lowC - minLow) / span).toFloat().coerceIn(0f, 1f),
                barWidthFraction = ((day.highC - day.lowC) / span).toFloat().coerceIn(0.08f, 1f),
            )
        }
    }

    private fun mapMetrics(forecast: Forecast, unit: TemperatureUnit): List<WeatherMetricUiModel> {
        val current = forecast.current
        return listOf(
            WeatherMetricUiModel(
                icon = UiIconType.THERMO,
                label = "Feels like",
                value = temperatureFormatter.degrees(current.apparentTemperatureC, unit),
                contentDescription = "Feels like ${temperatureFormatter.degrees(current.apparentTemperatureC, unit)}",
            ),
            WeatherMetricUiModel(
                icon = UiIconType.HUMIDITY,
                label = "Humidity",
                value = current.humidityPercent.toString(),
                unit = "%",
                contentDescription = "Humidity ${current.humidityPercent} percent",
            ),
            WeatherMetricUiModel(
                icon = UiIconType.WIND,
                label = "Wind",
                value = current.windSpeedKmh.roundToInt().toString(),
                unit = "km/h",
                contentDescription = "Wind ${current.windSpeedKmh.roundToInt()} kilometers per hour",
            ),
            WeatherMetricUiModel(
                icon = UiIconType.PRESSURE,
                label = "Pressure",
                value = current.pressureHpa.roundToInt().toString(),
                unit = "hPa",
                contentDescription = "Pressure ${current.pressureHpa.roundToInt()} hectopascal",
            ),
        )
    }

    private fun DailyForecast.shortDayLabel(): String =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
}