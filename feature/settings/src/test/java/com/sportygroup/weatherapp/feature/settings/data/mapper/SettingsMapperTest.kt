package com.sportygroup.weatherapp.feature.settings.data.mapper

import com.sportygroup.weatherapp.feature.settings.data.model.SettingsDataModel
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsMapperTest {

    private val mapper = SettingsMapper()

    @Test
    fun `null persisted values map to defaults`() {
        val result = mapper.toDomain(SettingsDataModel(null, null, null))
        assertEquals(AppSettings.DEFAULT, result)
    }

    @Test
    fun `known persisted values map to domain enums`() {
        val result = mapper.toDomain(SettingsDataModel("imperial", "fahrenheit", "dark"))
        assertEquals(MeasurementSystem.IMPERIAL, result.measurementSystem)
        assertEquals(TemperatureUnit.FAHRENHEIT, result.temperatureUnit)
        assertEquals(ThemeMode.DARK, result.themeMode)
    }

    @Test
    fun `unknown persisted values fall back to defaults`() {
        val result = mapper.toDomain(SettingsDataModel("???", "kelvin", "neon"))
        assertEquals(AppSettings.DEFAULT, result)
    }

    @Test
    fun `domain maps to stable string keys`() {
        val data = mapper.toData(
            AppSettings(
                measurementSystem = MeasurementSystem.IMPERIAL,
                temperatureUnit = TemperatureUnit.FAHRENHEIT,
                themeMode = ThemeMode.LIGHT,
            ),
        )
        assertEquals("imperial", data.measurementSystem)
        assertEquals("fahrenheit", data.temperatureUnit)
        assertEquals("light", data.themeMode)
    }

    @Test
    fun `round trip preserves values`() {
        val original = AppSettings(
            measurementSystem = MeasurementSystem.IMPERIAL,
            temperatureUnit = TemperatureUnit.CELSIUS,
            themeMode = ThemeMode.DARK,
        )
        assertEquals(original, mapper.toDomain(mapper.toData(original)))
    }
}