package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.ForecastSource
import com.sportygroup.weatherapp.core.model.HourlyForecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ForecastCacheMapperTest {

    private val mapper = ForecastCacheMapperImpl()

    private val forecast = Forecast(
        city = City("Malaga", "Spain", Coordinates(36.7, -4.4)),
        current = CurrentWeather(
            temperature = 24.0,
            apparentTemperature = 26.0,
            condition = WeatherCondition.PARTLY_CLOUDY,
            humidityPercent = 58,
            windSpeed = 12.0,
            pressureHpa = 1014.0,
            high = 26.0,
            low = 17.0,
            updatedAt = LocalDateTime.of(2026, 6, 5, 12, 0),
        ),
        hourly = listOf(
            HourlyForecast(LocalDateTime.of(2026, 6, 5, 12, 0), 24.0, WeatherCondition.PARTLY_CLOUDY, 10),
            HourlyForecast(LocalDateTime.of(2026, 6, 5, 13, 0), 25.0, WeatherCondition.CLEAR_DAY, 0),
        ),
        daily = listOf(
            DailyForecast(LocalDate.of(2026, 6, 5), WeatherCondition.PARTLY_CLOUDY, 26.0, 17.0, 10),
        ),
    )

    @Test
    fun `round-trips a forecast through the cache model`() {
        val restored = mapper.toDomain(mapper.toCache(forecast, cachedAtEpochMillis = 123L), isStale = false)

        assertEquals(forecast.city, restored.city)
        assertEquals(forecast.current, restored.current)
        assertEquals(forecast.hourly, restored.hourly)
        assertEquals(forecast.daily, restored.daily)
    }

    @Test
    fun `restored forecast is tagged as cache with the given staleness`() {
        val fresh = mapper.toDomain(mapper.toCache(forecast, 0L), isStale = false)
        val stale = mapper.toDomain(mapper.toCache(forecast, 0L), isStale = true)

        assertEquals(ForecastSource.CACHE, fresh.source)
        assertEquals(false, fresh.isStale)
        assertTrue(stale.isStale)
    }

    @Test
    fun `cached-at timestamp is preserved in the cache model`() {
        assertEquals(987L, mapper.toCache(forecast, cachedAtEpochMillis = 987L).cachedAtEpochMillis)
    }
}