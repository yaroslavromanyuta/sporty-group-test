package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.StringResources
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.R
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

class ForecastDomainToUiMapperImpl @Inject constructor(
    private val temperatureFormatter: TemperatureFormatter,
    private val measurementFormatter: MeasurementFormatter,
    private val conditionMapper: WeatherConditionUiMapper,
    private val stringResources: StringResources,
) : ForecastDomainToUiMapper {

    override fun map(forecast: Forecast, measurementSystem: MeasurementSystem): ForecastUiModel {
        val current = forecast.current
        val conditionLabel = conditionMapper.label(current.condition)
        val cityName = forecast.city.name.ifEmpty {
            stringResources.getString(R.string.forecast_current_location)
        }
        return ForecastUiModel(
            cityName = cityName,
            region = forecast.city.region,
            isCurrentLocation = forecast.city.isCurrentLocation,
            updatedLabel = stringResources.getString(
                R.string.forecast_updated,
                current.updatedAt.hour,
                current.updatedAt.minute,
            ),
            current = CurrentWeatherUiModel(
                temperature = temperatureFormatter.value(current.temperature),
                conditionLabel = conditionLabel,
                highLabel = temperatureFormatter.degrees(current.high),
                lowLabel = temperatureFormatter.degrees(current.low),
                weatherType = conditionMapper.weatherType(current.condition),
                contentDescription = stringResources.getString(
                    R.string.forecast_cd_current_weather,
                    conditionLabel,
                    temperatureFormatter.degrees(current.temperature),
                ),
            ),
            hourly = mapHourly(forecast),
            daily = mapDaily(forecast),
            metrics = mapMetrics(forecast, measurementSystem),
        )
    }

    private fun mapHourly(forecast: Forecast): List<HourlyForecastUiModel> {
        val currentHour = forecast.current.updatedAt.truncatedTo(ChronoUnit.HOURS)
        val upcoming = forecast.hourly.filter { !it.time.isBefore(currentHour) }
            .ifEmpty { forecast.hourly }
            .take(24)
        return upcoming.mapIndexed { index, hour ->
            HourlyForecastUiModel(
                timeLabel = if (index == 0) {
                    stringResources.getString(R.string.forecast_label_now)
                } else {
                    stringResources.getString(R.string.forecast_hour_format, hour.time.hour)
                },
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
        val today = forecast.current.updatedAt.toLocalDate()
        return days.map { day ->
            val isToday = day.date == today
            DailyForecastUiModel(
                dayLabel = if (isToday) {
                    stringResources.getString(R.string.forecast_label_today)
                } else {
                    day.shortDayLabel()
                },
                isToday = isToday,
                conditionLabel = conditionMapper.label(day.condition),
                weatherType = conditionMapper.weatherType(day.condition),
                highLabel = temperatureFormatter.degrees(day.high),
                lowLabel = temperatureFormatter.degrees(day.low),
                precipitationProbability = day.precipitationProbability,
                barStartFraction = ((day.low - minLow) / span).toFloat().coerceIn(0f, 1f),
                barWidthFraction = ((day.high - day.low) / span).toFloat().coerceIn(0f, 1f),
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
        val feelsLike = temperatureFormatter.degrees(current.apparentTemperature)
        return listOf(
            WeatherMetricUiModel(
                icon = UiIconType.THERMO,
                label = stringResources.getString(R.string.forecast_metric_feels_like),
                value = feelsLike,
                contentDescription = stringResources.getString(R.string.forecast_cd_feels_like, feelsLike),
            ),
            WeatherMetricUiModel(
                icon = UiIconType.HUMIDITY,
                label = stringResources.getString(R.string.forecast_metric_humidity),
                value = current.humidityPercent.toString(),
                unit = "%",
                contentDescription = stringResources.getString(
                    R.string.forecast_cd_humidity,
                    current.humidityPercent,
                ),
            ),
            WeatherMetricUiModel(
                icon = UiIconType.WIND,
                label = stringResources.getString(R.string.forecast_metric_wind),
                value = wind.value,
                unit = wind.unit,
                contentDescription = stringResources.getString(
                    R.string.forecast_cd_wind,
                    wind.value,
                    wind.unit,
                ),
            ),
            WeatherMetricUiModel(
                icon = UiIconType.PRESSURE,
                label = stringResources.getString(R.string.forecast_metric_pressure),
                value = pressure.value,
                unit = pressure.unit,
                contentDescription = stringResources.getString(
                    R.string.forecast_cd_pressure,
                    pressure.value,
                    pressure.unit,
                ),
            ),
        )
    }

    private fun DailyForecast.shortDayLabel(): String =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
}
