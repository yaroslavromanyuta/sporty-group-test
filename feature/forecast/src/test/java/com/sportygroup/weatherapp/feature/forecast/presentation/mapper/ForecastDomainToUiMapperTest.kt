package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.HourlyForecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import com.sportygroup.weatherapp.feature.forecast.presentation.model.TemperatureUnit
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeDateTimeProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ForecastDomainToUiMapperTest {

    private val now = LocalDateTime.of(2026, 6, 5, 12, 0)
    private lateinit var mapper: DefaultForecastDomainToUiMapper

    @Before
    fun setUp() {
        mapper = DefaultForecastDomainToUiMapper(
            temperatureFormatter = TemperatureFormatter(),
            conditionMapper = WeatherConditionUiMapper(),
            dateTimeProvider = FakeDateTimeProvider(now),
        )
    }

    private fun forecast(): Forecast = Forecast(
        city = City("Malaga", "Andalusia, Spain", Coordinates(36.7, -4.4), isCurrentLocation = true),
        current = CurrentWeather(
            temperatureC = 24.0,
            apparentTemperatureC = 26.0,
            condition = WeatherCondition.PARTLY_CLOUDY,
            humidityPercent = 58,
            windSpeedKmh = 12.0,
            pressureHpa = 1014.0,
            highC = 26.0,
            lowC = 17.0,
            updatedAt = now,
        ),
        hourly = listOf(
            HourlyForecast(now.minusHours(2), 20.0, WeatherCondition.CLOUDY, 0),
            HourlyForecast(now, 24.0, WeatherCondition.PARTLY_CLOUDY, 10),
            HourlyForecast(now.plusHours(1), 25.0, WeatherCondition.CLEAR_DAY, 0),
        ),
        daily = listOf(
            DailyForecast(LocalDate.of(2026, 6, 5), WeatherCondition.PARTLY_CLOUDY, 26.0, 17.0, 10),
            DailyForecast(LocalDate.of(2026, 6, 6), WeatherCondition.CLEAR_DAY, 28.0, 18.0, 0),
        ),
    )

    @Test
    fun `formats celsius temperatures`() {
        val ui = mapper.map(forecast(), TemperatureUnit.CELSIUS)
        assertEquals("24", ui.current.temperature)
        assertEquals("26°", ui.current.highLabel)
        assertEquals("17°", ui.current.lowLabel)
    }

    @Test
    fun `converts to fahrenheit when requested`() {
        val ui = mapper.map(forecast(), TemperatureUnit.FAHRENHEIT)
        // 24C -> 75F
        assertEquals("75", ui.current.temperature)
    }

    @Test
    fun `hourly starts at now and drops past hours`() {
        val ui = mapper.map(forecast(), TemperatureUnit.CELSIUS)
        assertEquals(2, ui.hourly.size)
        assertEquals("Now", ui.hourly.first().timeLabel)
        assertTrue(ui.hourly.first().isNow)
    }

    @Test
    fun `today is labelled and flagged`() {
        val ui = mapper.map(forecast(), TemperatureUnit.CELSIUS)
        assertEquals("Today", ui.daily.first().dayLabel)
        assertTrue(ui.daily.first().isToday)
    }

    @Test
    fun `builds four detail metrics`() {
        val ui = mapper.map(forecast(), TemperatureUnit.CELSIUS)
        assertEquals(4, ui.metrics.size)
        assertEquals("Feels like", ui.metrics.first().label)
    }
}