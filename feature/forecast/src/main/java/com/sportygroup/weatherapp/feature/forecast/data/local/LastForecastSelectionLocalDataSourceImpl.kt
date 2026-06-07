package com.sportygroup.weatherapp.feature.forecast.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sportygroup.weatherapp.feature.forecast.di.ForecastCacheDataStore
import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * DataStore-backed store for the last forecast selection, serialized as a single JSON
 * [LastForecastSelectionDataModel] entry. Returns null on an absent or corrupt value.
 */
class LastForecastSelectionLocalDataSourceImpl @Inject constructor(
    @ForecastCacheDataStore private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val mapper: LastForecastSelectionMapper,
) : LastForecastSelectionLocalDataSource {

    override suspend fun read(): LastForecastSelection? =
        dataStore.data.map { prefs -> prefs[KEY]?.let(::decode)?.let(mapper::toDomain) }.first()

    override suspend fun save(selection: LastForecastSelection) {
        val payload = json.encodeToString(
            LastForecastSelectionDataModel.serializer(),
            mapper.toData(selection),
        )
        dataStore.edit { prefs -> prefs[KEY] = payload }
    }

    private fun decode(payload: String): LastForecastSelectionDataModel? =
        runCatching {
            json.decodeFromString(LastForecastSelectionDataModel.serializer(), payload)
        }.getOrNull()

    private companion object {
        val KEY = stringPreferencesKey("last_forecast_selection")
    }
}
