package com.sportygroup.weatherapp.feature.settings.presentation.model

import androidx.compose.runtime.Immutable
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode

/** Display-ready settings selections for the Settings screen. */
@Immutable
data class SettingsUiModel(
    val measurementSystem: MeasurementSystem,
    val temperatureUnit: TemperatureUnit,
    val themeMode: ThemeMode,
)