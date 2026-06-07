package com.sportygroup.weatherapp.feature.forecast.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.di.ForecastCacheDataStore
import com.sportygroup.weatherapp.feature.forecast.domain.ForecastCachePolicy
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * DataStore-backed forecast cache. Each entry is a JSON [CachedForecastDataModel] stored under a
 * preferences key derived from the [ForecastCacheKey]. Reads tolerate corrupt/old payloads by
 * returning null rather than throwing.
 */
class ForecastLocalDataSourceImpl @Inject constructor(
    @ForecastCacheDataStore private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val mapper: ForecastCacheMapper,
    private val dateTimeProvider: DateTimeProvider,
) : ForecastLocalDataSource {

    override suspend fun save(key: ForecastCacheKey, forecast: Forecast) {
        val cached = mapper.toCache(forecast, nowEpochMillis())
        val payload = json.encodeToString(CachedForecastDataModel.serializer(), cached)
        dataStore.edit { prefs -> prefs[preferenceKey(key)] = payload }
    }

    override suspend fun read(key: ForecastCacheKey): Forecast? {
        val payload = dataStore.data.first()[preferenceKey(key)] ?: return null
        val cached = runCatching {
            json.decodeFromString(CachedForecastDataModel.serializer(), payload)
        }.getOrNull() ?: return null
        val isStale = !ForecastCachePolicy.isFresh(cached.cachedAtEpochMillis, nowEpochMillis())
        return mapper.toDomain(cached, isStale)
    }

    private fun preferenceKey(key: ForecastCacheKey) = stringPreferencesKey(PREFIX + key.value)

    private fun nowEpochMillis(): Long =
        dateTimeProvider.now().atZone(dateTimeProvider.zone()).toInstant().toEpochMilli()

    private companion object {
        const val PREFIX = "forecast_cache:"
    }
}