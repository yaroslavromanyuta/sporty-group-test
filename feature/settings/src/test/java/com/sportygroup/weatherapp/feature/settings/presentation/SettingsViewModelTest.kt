package com.sportygroup.weatherapp.feature.settings.presentation

import com.sportygroup.weatherapp.feature.settings.presentation.mapper.SettingsUiMapper
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiAction
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import com.sportygroup.weatherapp.lib.settings.usecase.ObserveSettingsUseCase
import com.sportygroup.weatherapp.lib.settings.usecase.UpdateSettingsUseCase
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val observeSettings = mockk<ObserveSettingsUseCase>()
    private val updateSettings = mockk<UpdateSettingsUseCase>(relaxed = true)
    private val settingsFlow = MutableStateFlow(AppSettings.DEFAULT)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { observeSettings() } returns settingsFlow
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel() =
        SettingsViewModel(observeSettings, updateSettings, SettingsUiMapper())

    @Test
    fun `state reflects observed settings`() = runTest {
        settingsFlow.value = AppSettings(themeMode = ThemeMode.DARK)
        val vm = viewModel()
        assertEquals(ThemeMode.DARK, vm.uiState.value.settings.themeMode)
    }

    @Test
    fun `selecting measurement system persists updated settings`() = runTest {
        val vm = viewModel()

        vm.onAction(SettingsUiAction.SelectMeasurementSystem(MeasurementSystem.IMPERIAL))

        coVerify {
            updateSettings(AppSettings.DEFAULT.copy(measurementSystem = MeasurementSystem.IMPERIAL))
        }
    }

    @Test
    fun `selecting same value does not persist`() = runTest {
        coEvery { updateSettings(any()) } returns Unit
        val vm = viewModel()

        vm.onAction(SettingsUiAction.SelectTemperatureUnit(AppSettings.DEFAULT.temperatureUnit))

        coVerify(exactly = 0) { updateSettings(any()) }
    }
}