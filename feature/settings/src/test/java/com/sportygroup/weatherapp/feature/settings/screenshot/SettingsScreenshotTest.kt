package com.sportygroup.weatherapp.feature.settings.screenshot

import com.sportygroup.weatherapp.core.designsystem.screenshot.RoborazziScreenshotTest
import com.sportygroup.weatherapp.core.designsystem.screenshot.ThemeVariant
import com.sportygroup.weatherapp.feature.settings.presentation.SettingsScreen
import com.sportygroup.weatherapp.feature.settings.presentation.component.SegmentOption
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsDivider
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsSection
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsSegmentedControl
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsThemePicker
import com.sportygroup.weatherapp.feature.settings.presentation.model.SettingsUiModel
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiState
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import org.junit.Test

/** Screenshot coverage for the settings feature: the screen and its components. */
class SettingsScreenshotTest(variant: ThemeVariant) : RoborazziScreenshotTest(variant) {

    @Test
    fun screenMetric() = snapshot("screenMetric") {
        SettingsScreen(
            state = SettingsUiState(
                SettingsUiModel(
                    measurementSystem = MeasurementSystem.METRIC,
                    temperatureUnit = TemperatureUnit.CELSIUS,
                    themeMode = ThemeMode.SYSTEM,
                ),
            ),
            onAction = {},
            onBack = {},
        )
    }

    @Test
    fun screenImperial() = snapshot("screenImperial") {
        SettingsScreen(
            state = SettingsUiState(
                SettingsUiModel(
                    measurementSystem = MeasurementSystem.IMPERIAL,
                    temperatureUnit = TemperatureUnit.FAHRENHEIT,
                    themeMode = ThemeMode.DARK,
                ),
            ),
            onAction = {},
            onBack = {},
        )
    }

    @Test
    fun section() = snapshot("section") {
        SettingsSection(label = "Units") {
            SettingsSegmentedControl(
                label = "Measurement system",
                helper = "Affects wind speed, pressure, and visibility",
                options = listOf(
                    SegmentOption(MeasurementSystem.METRIC, "Metric"),
                    SegmentOption(MeasurementSystem.IMPERIAL, "Imperial"),
                ),
                selected = MeasurementSystem.METRIC,
                onSelect = {},
            )
            SettingsDivider()
            SettingsSegmentedControl(
                label = "Temperature",
                options = listOf(
                    SegmentOption(TemperatureUnit.CELSIUS, "Celsius"),
                    SegmentOption(TemperatureUnit.FAHRENHEIT, "Fahrenheit"),
                ),
                selected = TemperatureUnit.CELSIUS,
                onSelect = {},
            )
        }
    }

    @Test
    fun segmentedControl() = snapshot("segmentedControl") {
        SettingsSegmentedControl(
            label = "Temperature",
            options = listOf(
                SegmentOption(TemperatureUnit.CELSIUS, "Celsius"),
                SegmentOption(TemperatureUnit.FAHRENHEIT, "Fahrenheit"),
            ),
            selected = TemperatureUnit.CELSIUS,
            onSelect = {},
        )
    }

    @Test
    fun themePicker() = snapshot("themePicker") {
        SettingsThemePicker(selected = ThemeMode.SYSTEM, onSelect = {})
    }
}
