package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.DailyForecast
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.HourlyForecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeDateTimeProvider
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
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
            measurementFormatter = MeasurementFormatter(),
            conditionMapper = WeatherConditionUiMapper(),
            dateTimeProvider = FakeDateTimeProvider(now),
        )
    }

    private fun forecast(): Forecast = Forecast(
        city = City("Malaga", "Andalusia, Spain", Coordinates(36.7, -4.4), isCurrentLocation = true),
        current = CurrentWeather(
            temperature = 24.0,
            apparentTemperature = 26.0,
            condition = WeatherCondition.PARTLY_CLOUDY,
            humidityPercent = 58,
            windSpeed = 12.0,
            pressureHpa = 1014.0,
            high = 26.0,
            low = 17.0,
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
    fun `formats temperatures without converting`() {
        val ui = mapper.map(forecast(), MeasurementSystem.METRIC)
        assertEquals("24", ui.current.temperature)
        assertEquals("26°", ui.current.highLabel)
        assertEquals("17°", ui.current.lowLabel)
    }

    @Test
    fun `metric wind and pressure labels`() {
        val ui = mapper.map(forecast(), MeasurementSystem.METRIC)
        val wind = ui.metrics.first { it.label == "Wind" }
        val pressure = ui.metrics.first { it.label == "Pressure" }
        assertEquals("12", wind.value)
        assertEquals("km/h", wind.unit)
        assertEquals("1014", pressure.value)
        assertEquals("hPa", pressure.unit)
    }

    @Test
    fun `imperial converts pressure and labels wind as mph`() {
        val ui = mapper.map(forecast(), MeasurementSystem.IMPERIAL)
        val wind = ui.metrics.first { it.label == "Wind" }
        val pressure = ui.metrics.first { it.label == "Pressure" }
        assertEquals("mph", wind.unit)
        assertEquals("inHg", pressure.unit)
        // 1014 hPa -> ~29.94 inHg
        assertEquals("29.94", pressure.value)
    }

    @Test
    fun `hourly starts at now and drops past hours`() {
        val ui = mapper.map(forecast(), MeasurementSystem.METRIC)
        assertEquals(2, ui.hourly.size)
        assertEquals("Now", ui.hourly.first().timeLabel)
        assertTrue(ui.hourly.first().isNow)
    }

    @Test
    fun `today is labelled and flagged`() {
        val ui = mapper.map(forecast(), MeasurementSystem.METRIC)
        assertEquals("Today", ui.daily.first().dayLabel)
        assertTrue(ui.daily.first().isToday)
    }
}