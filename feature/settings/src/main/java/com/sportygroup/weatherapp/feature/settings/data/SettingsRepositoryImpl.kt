package com.sportygroup.weatherapp.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sportygroup.weatherapp.feature.settings.data.mapper.SettingsMapper
import com.sportygroup.weatherapp.feature.settings.data.model.SettingsDataModel
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val mapper: SettingsMapper,
) : SettingsRepository {

    override fun observe(): Flow<AppSettings> = dataStore.data.map { prefs ->
        mapper.toDomain(
            SettingsDataModel(
                measurementSystem = prefs[KEY_MEASUREMENT],
                temperatureUnit = prefs[KEY_TEMPERATURE],
                themeMode = prefs[KEY_THEME],
            ),
        )
    }

    override suspend fun update(settings: AppSettings) {
        val data = mapper.toData(settings)
        dataStore.edit { prefs ->
            data.measurementSystem?.let { prefs[KEY_MEASUREMENT] = it }
            data.temperatureUnit?.let { prefs[KEY_TEMPERATURE] = it }
            data.themeMode?.let { prefs[KEY_THEME] = it }
        }
    }

    private companion object {
        val KEY_MEASUREMENT = stringPreferencesKey("measurement_system")
        val KEY_TEMPERATURE = stringPreferencesKey("temperature_unit")
        val KEY_THEME = stringPreferencesKey("theme_mode")
    }
}