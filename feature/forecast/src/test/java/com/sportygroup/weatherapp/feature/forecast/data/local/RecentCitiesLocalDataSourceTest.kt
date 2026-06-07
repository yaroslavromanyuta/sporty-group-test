package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.testutil.InMemoryPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecentCitiesLocalDataSourceTest {

    private lateinit var dataStore: InMemoryPreferencesDataStore

    private val malaga = City("Malaga", "Spain", Coordinates(36.7, -4.4))
    private val lisbon = City("Lisbon", "Portugal", Coordinates(38.72, -9.14))

    @Before
    fun setUp() {
        dataStore = InMemoryPreferencesDataStore()
    }

    private fun dataSource() = RecentCitiesLocalDataSourceImpl(
        dataStore = dataStore,
        json = Json { ignoreUnknownKeys = true; explicitNulls = false },
    )

    @Test
    fun `adding a city stores it`() = runTest {
        val ds = dataSource()
        ds.add(malaga)

        assertEquals(listOf(malaga), ds.observe().first())
    }

    @Test
    fun `most recently added city is first`() = runTest {
        val ds = dataSource()
        ds.add(malaga)
        ds.add(lisbon)

        assertEquals(listOf(lisbon, malaga), ds.observe().first())
    }

    @Test
    fun `re-adding an existing city moves it to the top without duplicating`() = runTest {
        val ds = dataSource()
        ds.add(malaga)
        ds.add(lisbon)
        ds.add(malaga)

        assertEquals(listOf(malaga, lisbon), ds.observe().first())
    }

    @Test
    fun `recent list is capped at five`() = runTest {
        val ds = dataSource()
        repeat(7) { i -> ds.add(City("City$i", "Region", Coordinates(i.toDouble(), i.toDouble()))) }

        val recent = ds.observe().first()
        assertEquals(5, recent.size)
        assertEquals("City6", recent.first().name)
        assertEquals("City2", recent.last().name)
    }

    @Test
    fun `current-location entries are not stored`() = runTest {
        val ds = dataSource()
        ds.add(City("Here", "", Coordinates(1.0, 2.0), isCurrentLocation = true))

        assertTrue(ds.observe().first().isEmpty())
    }

    @Test
    fun `recents persist across data source instances backed by the same store`() = runTest {
        dataSource().add(malaga)

        // A fresh data source over the same DataStore sees the persisted entry.
        assertEquals(listOf(malaga), dataSource().observe().first())
    }
}