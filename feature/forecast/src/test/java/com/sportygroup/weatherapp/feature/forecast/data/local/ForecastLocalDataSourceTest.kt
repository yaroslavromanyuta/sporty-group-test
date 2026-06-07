package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.ForecastSource
import com.sportygroup.weatherapp.core.model.HourlyForecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import com.sportygroup.weatherapp.feature.forecast.testutil.InMemoryPreferencesDataStore
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class ForecastLocalDataSourceTest {

    private lateinit var clock: MutableClock
    private lateinit var dataSource: ForecastLocalDataSourceImpl

    private val units = ForecastUnits("celsius", "kmh", "mm")
    private val malaga = City("Malaga", "Spain", Coordinates(36.7, -4.4))
    private val key get() = ForecastCacheKey.from(malaga, units)

    private val forecast = Forecast(
        city = malaga,
        current = CurrentWeather(24.0, 26.0, WeatherCondition.PARTLY_CLOUDY, 58, 12.0, 1014.0, 26.0, 17.0, LocalDateTime.of(2026, 6, 5, 12, 0)),
        hourly = listOf(HourlyForecast(LocalDateTime.of(2026, 6, 5, 12, 0), 24.0, WeatherCondition.PARTLY_CLOUDY, 10)),
        daily = listOf(DailyForecast(LocalDate.of(2026, 6, 5), WeatherCondition.PARTLY_CLOUDY, 26.0, 17.0, 10)),
    )

    @Before
    fun setUp() {
        clock = MutableClock(LocalDateTime.of(2026, 6, 5, 12, 0))
        dataSource = ForecastLocalDataSourceImpl(
            dataStore = InMemoryPreferencesDataStore(),
            json = Json { ignoreUnknownKeys = true; explicitNulls = false },
            mapper = ForecastCacheMapperImpl(),
            dateTimeProvider = clock,
        )
    }

    @Test
    fun `read returns null when nothing was saved`() = runTest {
        assertNull(dataSource.read(key))
    }

    @Test
    fun `saved forecast is read back as cache source`() = runTest {
        dataSource.save(key, forecast)

        val cached = dataSource.read(key)!!

        assertEquals(ForecastSource.CACHE, cached.source)
        assertEquals(forecast.current, cached.current)
        assertEquals(forecast.city, cached.city)
    }

    @Test
    fun `fresh cache is not flagged stale`() = runTest {
        dataSource.save(key, forecast)
        clock.advanceMinutes(10)

        assertFalse(dataSource.read(key)!!.isStale)
    }

    @Test
    fun `cache older than the freshness window is flagged stale but still returned`() = runTest {
        dataSource.save(key, forecast)
        clock.advanceMinutes(31)

        val cached = dataSource.read(key)!!

        assertTrue(cached.isStale)
        assertEquals(ForecastSource.CACHE, cached.source)
    }

    @Test
    fun `different units do not share a cache entry`() = runTest {
        dataSource.save(key, forecast)

        val imperialKey = ForecastCacheKey.from(malaga, ForecastUnits("fahrenheit", "mph", "inch"))

        assertNull(dataSource.read(imperialKey))
    }

    private class MutableClock(private var current: LocalDateTime) : DateTimeProvider {
        override fun now(): LocalDateTime = current
        override fun zone(): ZoneId = ZoneId.of("UTC")
        fun advanceMinutes(minutes: Long) {
            current = current.plusMinutes(minutes)
        }
    }
}