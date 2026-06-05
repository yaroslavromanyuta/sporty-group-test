package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.core.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherCodeMapperTest {

    @Test
    fun `clear sky maps to clear day`() {
        assertEquals(WeatherCondition.CLEAR_DAY, WeatherCodeMapper.toCondition(0))
    }

    @Test
    fun `partly cloudy codes map to partly cloudy`() {
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherCodeMapper.toCondition(1))
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherCodeMapper.toCondition(2))
    }

    @Test
    fun `rain codes map to rain`() {
        assertEquals(WeatherCondition.RAIN, WeatherCodeMapper.toCondition(61))
        assertEquals(WeatherCondition.RAIN, WeatherCodeMapper.toCondition(65))
    }

    @Test
    fun `thunderstorm codes map to thunderstorm`() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCodeMapper.toCondition(95))
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCodeMapper.toCondition(99))
    }

    @Test
    fun `unknown code maps to unknown`() {
        assertEquals(WeatherCondition.UNKNOWN, WeatherCodeMapper.toCondition(-1))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCodeMapper.toCondition(12345))
    }
}