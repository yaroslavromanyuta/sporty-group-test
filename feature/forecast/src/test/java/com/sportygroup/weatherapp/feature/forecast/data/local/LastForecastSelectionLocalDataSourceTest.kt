package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import com.sportygroup.weatherapp.feature.forecast.testutil.InMemoryPreferencesDataStore
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LastForecastSelectionLocalDataSourceTest {

    private lateinit var dataStore: InMemoryPreferencesDataStore

    private val lisbon = City("Lisbon", "Portugal", Coordinates(38.72, -9.14))

    @Before
    fun setUp() {
        dataStore = InMemoryPreferencesDataStore()
    }

    private fun dataSource() = LastForecastSelectionLocalDataSourceImpl(
        dataStore = dataStore,
        json = Json { ignoreUnknownKeys = true; explicitNulls = false },
        mapper = LastForecastSelectionMapperImpl(),
    )

    @Test
    fun `returns null when nothing has been saved`() = runTest {
        assertNull(dataSource().read())
    }

    @Test
    fun `saves and reads current location`() = runTest {
        val ds = dataSource()
        ds.save(LastForecastSelection.CurrentLocation)

        assertEquals(LastForecastSelection.CurrentLocation, ds.read())
    }

    @Test
    fun `saves and reads a manual city`() = runTest {
        val ds = dataSource()
        ds.save(LastForecastSelection.ManualCity(lisbon))

        assertEquals(LastForecastSelection.ManualCity(lisbon), ds.read())
    }

    @Test
    fun `latest save overwrites the previous selection`() = runTest {
        val ds = dataSource()
        ds.save(LastForecastSelection.ManualCity(lisbon))
        ds.save(LastForecastSelection.CurrentLocation)

        assertEquals(LastForecastSelection.CurrentLocation, ds.read())
    }

    @Test
    fun `selection persists across data source instances backed by the same store`() = runTest {
        dataSource().save(LastForecastSelection.ManualCity(lisbon))

        assertEquals(LastForecastSelection.ManualCity(lisbon), dataSource().read())
    }
}
