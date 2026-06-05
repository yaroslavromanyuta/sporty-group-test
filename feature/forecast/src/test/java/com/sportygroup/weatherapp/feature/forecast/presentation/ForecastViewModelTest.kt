package com.sportygroup.weatherapp.feature.forecast.presentation

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.GetCurrentLocationForecastUseCase
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.GetForecastByCityUseCase
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.SearchCitiesUseCase
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.CityUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ForecastDomainToUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import com.sportygroup.weatherapp.lib.settings.usecase.ObserveSettingsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private val observeSettings = mockk<ObserveSettingsUseCase>()
    private val getCurrentLocationForecast = mockk<GetCurrentLocationForecastUseCase>()
    private val getForecastByCity = mockk<GetForecastByCityUseCase>()
    private val searchCities = mockk<SearchCitiesUseCase>()
    private val forecastUiMapper = mockk<ForecastDomainToUiMapper>()
    private val cityUiMapper = CityUiMapper()
    private val errorUiMapper = ErrorUiMapper()

    private val settingsFlow = MutableStateFlow(AppSettings.DEFAULT)

    private val domainForecast = Forecast(
        city = City("Malaga", "Spain", Coordinates(36.7, -4.4), isCurrentLocation = true),
        current = mockk(relaxed = true),
        hourly = emptyList(),
        daily = emptyList(),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { observeSettings() } returns settingsFlow
        every { forecastUiMapper.map(any(), any()) } returns ForecastPreviewData.forecast
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = ForecastViewModel(
        observeSettings,
        getCurrentLocationForecast,
        getForecastByCity,
        searchCities,
        forecastUiMapper,
        cityUiMapper,
        errorUiMapper,
    )

    @Test
    fun `successful location load emits content`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.Content)
    }

    @Test
    fun `missing permission emits permission required`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns
            AppResult.Failure(AppError.NoLocationPermission)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.PermissionRequired)
    }

    @Test
    fun `network failure emits error`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Failure(AppError.Network)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.Error)
    }

    @Test
    fun `changing measurement system reloads forecast`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()
        assertTrue(vm.uiState.value is ForecastUiState.Content)

        // a units change should trigger a second forecast load
        settingsFlow.value = AppSettings.DEFAULT.copy(measurementSystem = MeasurementSystem.IMPERIAL)

        coVerify(exactly = 2) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `theme-only change does not reload forecast`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()
        assertTrue(vm.uiState.value is ForecastUiState.Content)

        settingsFlow.value = AppSettings.DEFAULT.copy(themeMode = ThemeMode.DARK)

        coVerify(exactly = 1) { getCurrentLocationForecast(any()) }
    }
}