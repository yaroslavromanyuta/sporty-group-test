package com.sportygroup.weatherapp.feature.settings.presentation.mapper

import com.sportygroup.weatherapp.feature.settings.presentation.model.SettingsUiModel
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import javax.inject.Inject

/** Maps domain [AppSettings] to the presentation [SettingsUiModel]. */
class SettingsUiMapper @Inject constructor() {
    fun toUi(settings: AppSettings): SettingsUiModel = SettingsUiModel(
        measurementSystem = settings.measurementSystem,
        temperatureUnit = settings.temperatureUnit,
        themeMode = settings.themeMode,
    )
}