package com.sportygroup.weatherapp.feature.settings.presentation.component

import androidx.compose.runtime.Composable
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode

@ThemePreviews
@Composable
private fun SettingsSectionPreview() {
    SkyPreview {
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
}

@ThemePreviews
@Composable
private fun SettingsSegmentedControlPreview() {
    SkyPreview {
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

@ThemePreviews
@Composable
private fun SettingsThemePickerPreview() {
    SkyPreview {
        SettingsThemePicker(selected = ThemeMode.SYSTEM, onSelect = {})
    }
}