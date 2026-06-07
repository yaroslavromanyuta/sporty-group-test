package com.sportygroup.weatherapp.feature.settings.presentation.state

import com.sportygroup.weatherapp.feature.settings.presentation.model.SettingsUiModel
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode

/** State for the Settings screen. */
data class SettingsUiState(
    val settings: SettingsUiModel,
)

/** User intents from the Settings screen. */
sealed interface SettingsUiAction {
    data class SelectMeasurementSystem(val value: MeasurementSystem) : SettingsUiAction
    data class SelectTemperatureUnit(val value: TemperatureUnit) : SettingsUiAction
    data class SelectThemeMode(val value: ThemeMode) : SettingsUiAction
}