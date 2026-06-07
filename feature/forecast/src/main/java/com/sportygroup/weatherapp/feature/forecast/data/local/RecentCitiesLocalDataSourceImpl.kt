package com.sportygroup.weatherapp.feature.forecast.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.di.ForecastCacheDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * DataStore-backed recent-cities list, serialized as a JSON array of [CachedCityDataModel].
 * Deduplicates by location identity, keeps the most recent [MAX_RECENT] and never stores
 * current-location entries.
 */
class RecentCitiesLocalDataSourceImpl @Inject constructor(
    @ForecastCacheDataStore private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : RecentCitiesLocalDataSource {

    override fun observe(): Flow<List<City>> = dataStore.data.map { prefs ->
        prefs[KEY]?.let(::decode).orEmpty().map(::toDomain)
    }

    override suspend fun add(city: City) {
        if (city.isCurrentLocation) return
        val incoming = city.toCached()
        dataStore.edit { prefs ->
            val existing = prefs[KEY]?.let(::decode).orEmpty()
            val updated = (listOf(incoming) + existing)
                .distinctBy { identityOf(it) }
                .take(MAX_RECENT)
            prefs[KEY] = json.encodeToString(serializer, updated)
        }
    }

    private fun decode(payload: String): List<CachedCityDataModel> =
        runCatching { json.decodeFromString(serializer, payload) }.getOrDefault(emptyList())

    private fun identityOf(city: CachedCityDataModel): String =
        "${city.name.trim().lowercase()}|${city.region.trim().lowercase()}|" +
            "${city.latitude}|${city.longitude}"

    private fun toDomain(model: CachedCityDataModel): City = City(
        name = model.name,
        region = model.region,
        coordinates = Coordinates(model.latitude, model.longitude),
        isCurrentLocation = false,
    )

    private fun City.toCached(): CachedCityDataModel = CachedCityDataModel(
        name = name,
        region = region,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        isCurrentLocation = false,
    )

    private companion object {
        const val MAX_RECENT = 5
        val KEY = stringPreferencesKey("recent_cities")
        val serializer = ListSerializer(CachedCityDataModel.serializer())
    }
}