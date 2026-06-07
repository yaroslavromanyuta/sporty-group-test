package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.ForecastSource
import com.sportygroup.weatherapp.core.model.HourlyForecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class ForecastCacheMapperImpl @Inject constructor() : ForecastCacheMapper {

    override fun toCache(forecast: Forecast, cachedAtEpochMillis: Long): CachedForecastDataModel {
        val current = forecast.current
        return CachedForecastDataModel(
            city = CachedCityDataModel(
                name = forecast.city.name,
                region = forecast.city.region,
                latitude = forecast.city.coordinates.latitude,
                longitude = forecast.city.coordinates.longitude,
                isCurrentLocation = forecast.city.isCurrentLocation,
            ),
            current = CachedCurrentWeather(
                temperature = current.temperature,
                apparentTemperature = current.apparentTemperature,
                condition = current.condition.name,
                humidityPercent = current.humidityPercent,
                windSpeed = current.windSpeed,
                pressureHpa = current.pressureHpa,
                high = current.high,
                low = current.low,
                updatedAt = current.updatedAt.toString(),
            ),
            hourly = forecast.hourly.map { hour ->
                CachedHourlyForecast(
                    time = hour.time.toString(),
                    temperature = hour.temperature,
                    condition = hour.condition.name,
                    precipitationProbability = hour.precipitationProbability,
                )
            },
            daily = forecast.daily.map { day ->
                CachedDailyForecast(
                    date = day.date.toString(),
                    condition = day.condition.name,
                    high = day.high,
                    low = day.low,
                    precipitationProbability = day.precipitationProbability,
                )
            },
            cachedAtEpochMillis = cachedAtEpochMillis,
        )
    }

    override fun toDomain(model: CachedForecastDataModel, isStale: Boolean): Forecast = Forecast(
        city = City(
            name = model.city.name,
            region = model.city.region,
            coordinates = Coordinates(model.city.latitude, model.city.longitude),
            isCurrentLocation = model.city.isCurrentLocation,
        ),
        current = CurrentWeather(
            temperature = model.current.temperature,
            apparentTemperature = model.current.apparentTemperature,
            condition = model.current.condition.toCondition(),
            humidityPercent = model.current.humidityPercent,
            windSpeed = model.current.windSpeed,
            pressureHpa = model.current.pressureHpa,
            high = model.current.high,
            low = model.current.low,
            updatedAt = LocalDateTime.parse(model.current.updatedAt),
        ),
        hourly = model.hourly.map { hour ->
            HourlyForecast(
                time = LocalDateTime.parse(hour.time),
                temperature = hour.temperature,
                condition = hour.condition.toCondition(),
                precipitationProbability = hour.precipitationProbability,
            )
        },
        daily = model.daily.map { day ->
            DailyForecast(
                date = LocalDate.parse(day.date),
                condition = day.condition.toCondition(),
                high = day.high,
                low = day.low,
                precipitationProbability = day.precipitationProbability,
            )
        },
        source = ForecastSource.CACHE,
        isStale = isStale,
    )

    private fun String.toCondition(): WeatherCondition =
        runCatching { WeatherCondition.valueOf(this) }.getOrDefault(WeatherCondition.UNKNOWN)
}