package com.sportygroup.weatherapp.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.sportygroup.weatherapp.feature.settings.data.mapper.SettingsMapper
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        scope = CoroutineScope(UnconfinedTestDispatcher())
        dataStore = PreferenceDataStoreFactory.create(scope = scope) {
            tmpFolder.newFile("settings.preferences_pb")
        }
        repository = SettingsRepositoryImpl(dataStore, SettingsMapper())
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `observe returns defaults when empty`() = runTest {
        assertEquals(AppSettings.DEFAULT, repository.observe().first())
    }

    @Test
    fun `update persists and is observable`() = runTest {
        val updated = AppSettings(
            measurementSystem = MeasurementSystem.IMPERIAL,
            temperatureUnit = TemperatureUnit.FAHRENHEIT,
            themeMode = ThemeMode.DARK,
        )

        repository.update(updated)

        assertEquals(updated, repository.observe().first())
    }
}