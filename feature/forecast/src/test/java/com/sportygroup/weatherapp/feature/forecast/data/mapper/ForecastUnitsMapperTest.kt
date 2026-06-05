package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastUnitsMapperTest {

    private val mapper = ForecastUnitsMapper()

    @Test
    fun `metric celsius maps to metric api params`() {
        val units = mapper.map(
            AppSettings(
                measurementSystem = MeasurementSystem.METRIC,
                temperatureUnit = TemperatureUnit.CELSIUS,
            ),
        )

        assertEquals("celsius", units.temperatureUnit)
        assertEquals("kmh", units.windSpeedUnit)
        assertEquals("mm", units.precipitationUnit)
    }

    @Test
    fun `imperial fahrenheit maps to imperial api params`() {
        val units = mapper.map(
            AppSettings(
                measurementSystem = MeasurementSystem.IMPERIAL,
                temperatureUnit = TemperatureUnit.FAHRENHEIT,
            ),
        )

        assertEquals("fahrenheit", units.temperatureUnit)
        assertEquals("mph", units.windSpeedUnit)
        assertEquals("inch", units.precipitationUnit)
    }

    @Test
    fun `temperature and measurement are independent`() {
        val units = mapper.map(
            AppSettings(
                measurementSystem = MeasurementSystem.METRIC,
                temperatureUnit = TemperatureUnit.FAHRENHEIT,
            ),
        )

        assertEquals("fahrenheit", units.temperatureUnit)
        assertEquals("kmh", units.windSpeedUnit)
    }
}