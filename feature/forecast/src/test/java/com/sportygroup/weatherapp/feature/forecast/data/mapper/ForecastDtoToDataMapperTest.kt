package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.CurrentDto
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.DailyDto
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.HourlyDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ForecastDtoToDataMapperTest {

    private lateinit var mapper: DefaultForecastDtoToDataMapper

    @Before
    fun setUp() {
        mapper = DefaultForecastDtoToDataMapper()
    }

    @Test
    fun `zips parallel hourly arrays into rows`() {
        val dto = ForecastResponseDto(
            hourly = HourlyDto(
                time = listOf("2026-06-05T12:00", "2026-06-05T13:00"),
                temperature = listOf(24.0, 25.0),
                weatherCode = listOf(0, 2),
                precipitationProbability = listOf(10, null),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(2, result.hourly.size)
        assertEquals(24.0, result.hourly[0].temperatureC, 0.0)
        assertEquals(10, result.hourly[0].precipitationProbability)
        assertEquals(25.0, result.hourly[1].temperatureC, 0.0)
        // null precipitation probability is coalesced to 0
        assertEquals(0, result.hourly[1].precipitationProbability)
    }

    @Test
    fun `coalesces null current fields to defaults`() {
        val dto = ForecastResponseDto(current = CurrentDto(temperature = 20.0))

        val result = mapper.map(dto)

        assertEquals(20.0, result.current.temperatureC, 0.0)
        // apparent temperature falls back to temperature when absent
        assertEquals(20.0, result.current.apparentTemperatureC, 0.0)
        assertEquals(0, result.current.humidityPercent)
    }

    @Test
    fun `maps daily highs and lows`() {
        val dto = ForecastResponseDto(
            daily = DailyDto(
                time = listOf("2026-06-05"),
                weatherCode = listOf(61),
                temperatureMax = listOf(26.0),
                temperatureMin = listOf(17.0),
                precipitationProbabilityMax = listOf(80),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.daily.size)
        assertEquals(26.0, result.daily[0].highC, 0.0)
        assertEquals(17.0, result.daily[0].lowC, 0.0)
        assertEquals(80, result.daily[0].precipitationProbability)
    }
}