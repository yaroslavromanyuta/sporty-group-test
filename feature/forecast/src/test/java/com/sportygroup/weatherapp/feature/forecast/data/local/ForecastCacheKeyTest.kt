package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ForecastCacheKeyTest {

    private val metric = ForecastUnits(temperatureUnit = "celsius", windSpeedUnit = "kmh", precipitationUnit = "mm")
    private val imperial = ForecastUnits(temperatureUnit = "fahrenheit", windSpeedUnit = "mph", precipitationUnit = "inch")
    private val malaga = City("Malaga", "Spain", Coordinates(36.7, -4.4))

    @Test
    fun `key encodes location and units`() {
        val key = ForecastCacheKey.from(malaga, metric).value

        assertEquals("36.7000|-4.4000|malaga|celsius|kmh", key)
    }

    @Test
    fun `different temperature unit yields a different key`() {
        val celsius = ForecastCacheKey.from(malaga, metric).value
        val fahrenheit = ForecastCacheKey.from(malaga, imperial).value

        assertNotEquals(celsius, fahrenheit)
    }

    @Test
    fun `different city yields a different key`() {
        val lisbon = City("Lisbon", "Portugal", Coordinates(38.72, -9.14))

        assertNotEquals(
            ForecastCacheKey.from(malaga, metric).value,
            ForecastCacheKey.from(lisbon, metric).value,
        )
    }

    @Test
    fun `same location with different name casing collapses to one key`() {
        val lower = malaga.copy(name = "malaga")
        val upper = malaga.copy(name = "MALAGA")

        assertEquals(
            ForecastCacheKey.from(lower, metric).value,
            ForecastCacheKey.from(upper, metric).value,
        )
    }

    @Test
    fun `current location uses coordinates and empty name`() {
        val current = City("", "", Coordinates(40.4, -3.7), isCurrentLocation = true)

        assertEquals("40.4000|-3.7000||celsius|kmh", ForecastCacheKey.from(current, metric).value)
    }
}