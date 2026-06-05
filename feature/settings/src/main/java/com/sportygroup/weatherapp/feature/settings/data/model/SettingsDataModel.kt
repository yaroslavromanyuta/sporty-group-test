package com.sportygroup.weatherapp.feature.settings.data.model

/** Raw, persistence-facing representation of settings (DataStore string values). */
data class SettingsDataModel(
    val measurementSystem: String?,
    val temperatureUnit: String?,
    val themeMode: String?,
)