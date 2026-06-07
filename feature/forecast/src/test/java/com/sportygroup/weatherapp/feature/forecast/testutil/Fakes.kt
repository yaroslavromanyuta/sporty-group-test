package com.sportygroup.weatherapp.feature.forecast.testutil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.common.StringResources
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.ForecastSource
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastCacheKey
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.local.RecentCitiesLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

/** Deterministic [DateTimeProvider] for tests. */
class FakeDateTimeProvider(
    private val now: LocalDateTime = LocalDateTime.of(2026, 6, 5, 12, 0),
) : DateTimeProvider {
    override fun now(): LocalDateTime = now
    override fun zone(): ZoneId = ZoneId.of("UTC")
}

/** [DispatcherProvider] backed by a single test dispatcher. */
class TestDispatcherProvider(
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}

/**
 * [StringResources] for tests. Returns mapped strings for known resource ids; for unmapped
 * ids it returns a deterministic placeholder so non-asserted strings stay stable.
 */
class FakeStringResources(
    private val strings: Map<Int, String> = emptyMap(),
) : StringResources {
    override fun getString(resId: Int): String = strings[resId] ?: "res:$resId"
    override fun getString(resId: Int, vararg formatArgs: Any): String =
        strings[resId]?.let { String.format(Locale.US, it, *formatArgs) }
            ?: "res:$resId:" + formatArgs.joinToString()
}

/**
 * In-memory [ForecastLocalDataSource] for repository tests. Stores the forecast as-given and,
 * on read, returns it tagged as [ForecastSource.CACHE] with a configurable staleness.
 */
class FakeForecastLocalDataSource : ForecastLocalDataSource {
    private val store = mutableMapOf<String, Forecast>()
    var staleOnRead: Boolean = false

    override suspend fun save(key: ForecastCacheKey, forecast: Forecast) {
        store[key.value] = forecast
    }

    override suspend fun read(key: ForecastCacheKey): Forecast? =
        store[key.value]?.copy(source = ForecastSource.CACHE, isStale = staleOnRead)
}

/**
 * In-memory [DataStore] of [Preferences] for tests. Avoids real file IO (and its platform-specific
 * file-locking quirks) so the local data sources can be exercised against the production code paths.
 */
class InMemoryPreferencesDataStore : DataStore<Preferences> {
    private val flow = MutableStateFlow(emptyPreferences())
    override val data: Flow<Preferences> = flow

    override suspend fun updateData(
        transform: suspend (t: Preferences) -> Preferences,
    ): Preferences = transform(flow.value).also { flow.value = it }
}

/** In-memory [RecentCitiesLocalDataSource] mirroring the production dedupe/cap/limit rules. */
class FakeRecentCitiesLocalDataSource(private val maxRecent: Int = 5) : RecentCitiesLocalDataSource {
    private val state = MutableStateFlow<List<City>>(emptyList())

    override fun observe(): Flow<List<City>> = state

    override suspend fun add(city: City) {
        if (city.isCurrentLocation) return
        state.value = (listOf(city) + state.value)
            .distinctBy { listOf(it.name.lowercase(), it.region.lowercase(), it.coordinates) }
            .take(maxRecent)
    }
}