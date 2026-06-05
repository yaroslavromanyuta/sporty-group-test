package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CurrentWeatherUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.DailyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.HourlyForecastUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.WeatherMetricUiModel
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

interface ForecastDomainToUiMapper {
    fun map(forecast: Forecast, measurementSystem: MeasurementSystem): ForecastUiModel
}

class DefaultForecastDomainToUiMapper @Inject constructor(
    private val temperatureFormatter: TemperatureFormatter,
    private val measurementFormatter: MeasurementFormatter,
    private val conditionMapper: WeatherConditionUiMapper,
    private val dateTimeProvider: DateTimeProvider,
) : ForecastDomainToUiMapper {

    override fun map(forecast: Forecast, measurementSystem: MeasurementSystem): ForecastUiModel {
        val current = forecast.current
        return ForecastUiModel(
            cityName = forecast.city.name,
            region = forecast.city.region,
            isCurrentLocation = forecast.city.isCurrentLocation,
            updatedLabel = "Updated %02d:%02d".format(current.updatedAt.hour, current.updatedAt.minute),
            current = CurrentWeatherUiModel(
                temperature = temperatureFormatter.value(current.temperature),
                conditionLabel = conditionMapper.label(current.condition),
                highLabel = temperatureFormatter.degrees(current.high),
                lowLabel = temperatureFormatter.degrees(current.low),
                weatherType = conditionMapper.weatherType(current.condition),
                contentDescription = "${conditionMapper.label(current.condition)}, " +
                    temperatureFormatter.degrees(current.temperature),
            ),
            hourly = mapHourly(forecast),
            daily = mapDaily(forecast),
            metrics = mapMetrics(forecast, measurementSystem),
        )
    }

    private fun mapHourly(forecast: Forecast): List<HourlyForecastUiModel> {
        val currentHour = dateTimeProvider.now().truncatedTo(ChronoUnit.HOURS)
        val upcoming = forecast.hourly.filter { !it.time.isBefore(currentHour) }
            .ifEmpty { forecast.hourly }
            .take(24)
        return upcoming.mapIndexed { index, hour ->
            HourlyForecastUiModel(
                timeLabel = if (index == 0) "Now" else "%02d:00".format(hour.time.hour),
                temperatureLabel = temperatureFormatter.degrees(hour.temperature),
                weatherType = conditionMapper.weatherType(hour.condition),
                precipitationProbability = hour.precipitationProbability,
                isNow = index == 0,
            )
        }
    }

    private fun mapDaily(forecast: Forecast): List<DailyForecastUiModel> {
        val days = forecast.daily
        if (days.isEmpty()) return emptyList()
        val minLow = days.minOf { it.low }
        val maxHigh = days.maxOf { it.high }
        val span = (maxHigh - minLow).takeIf { it != 0.0 } ?: 1.0
        val today = dateTimeProvider.now().toLocalDate()
        return days.map { day ->
            val isToday = day.date == today
            DailyForecastUiModel(
                dayLabel = if (isToday) "Today" else day.shortDayLabel(),
                isToday = isToday,
                conditionLabel = conditionMapper.label(day.condition),
                weatherType = conditionMapper.weatherType(day.condition),
                highLabel = temperatureFormatter.degrees(day.high),
                lowLabel = temperatureFormatter.degrees(day.low),
                precipitationProbability = day.precipitationProbability,
                barStartFraction = ((day.low - minLow) / span).toFloat().coerceIn(0f, 1f),
                barWidthFraction = ((day.high - day.low) / span).toFloat().coerceIn(0.08f, 1f),
            )
        }
    }

    private fun mapMetrics(
        forecast: Forecast,
        measurementSystem: MeasurementSystem,
    ): List<WeatherMetricUiModel> {
        val current = forecast.current
        val wind = measurementFormatter.wind(current.windSpeed, measurementSystem)
        val pressure = measurementFormatter.pressure(current.pressureHpa, measurementSystem)
        return listOf(
            WeatherMetricUiModel(
                icon = UiIconType.THERMO,
                label = "Feels like",
                value = temperatureFormatter.degrees(current.apparentTemperature),
                contentDescription = "Feels like ${temperatureFormatter.degrees(current.apparentTemperature)}",
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
                value = wind.value,
                unit = wind.unit,
                contentDescription = "Wind ${wind.value} ${wind.unit}",
            ),
            WeatherMetricUiModel(
                icon = UiIconType.PRESSURE,
                label = "Pressure",
                value = pressure.value,
                unit = pressure.unit,
                contentDescription = "Pressure ${pressure.value} ${pressure.unit}",
            ),
        )
    }

    private fun DailyForecast.shortDayLabel(): String =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
}