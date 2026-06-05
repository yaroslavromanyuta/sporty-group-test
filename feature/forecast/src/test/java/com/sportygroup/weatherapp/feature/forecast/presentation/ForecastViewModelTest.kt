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
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeStringResources
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    private val errorUiMapper = ErrorUiMapper(FakeStringResources())

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
    fun `starts on initial choice without loading on launch`() = runTest {
        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.InitialChoice)
        // No automatic location load / navigation on cold start.
        coVerify(exactly = 0) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `granting permission starts current location load and shows content`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()

        vm.onLocationPermissionGranted()

        coVerify(exactly = 1) { getCurrentLocationForecast(any()) }
        assertTrue(vm.uiState.value is ForecastUiState.Content)
    }

    @Test
    fun `denying permission keeps the first screen with a manual-search hint`() = runTest {
        val vm = viewModel()

        vm.onLocationPermissionDenied()

        val state = vm.uiState.value
        assertTrue(state is ForecastUiState.InitialChoice)
        assertTrue((state as ForecastUiState.InitialChoice).permissionDenied)
        assertTrue(state.canSearchManually)
        coVerify(exactly = 0) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `permanent denial flags the first screen to use settings`() = runTest {
        val vm = viewModel()

        vm.onLocationPermissionDenied(permanently = true)

        val state = vm.uiState.value
        assertTrue(state is ForecastUiState.InitialChoice)
        assertTrue((state as ForecastUiState.InitialChoice).permissionDenied)
        assertTrue(state.permissionPermanentlyDenied)
        assertTrue(state.canSearchManually)
    }

    @Test
    fun `permission becoming available clears denied flags on the first screen`() = runTest {
        val vm = viewModel()
        vm.onLocationPermissionDenied(permanently = true)

        // User enabled permission in system Settings and returned to the app.
        vm.onLocationPermissionAvailable()

        val state = vm.uiState.value
        assertTrue(state is ForecastUiState.InitialChoice)
        assertFalse((state as ForecastUiState.InitialChoice).permissionDenied)
        assertFalse(state.permissionPermanentlyDenied)
        // We do not auto-load; the user still taps "Use current location".
        coVerify(exactly = 0) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `permission available does not disturb loaded content`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()
        vm.onLocationPermissionGranted()
        assertTrue(vm.uiState.value is ForecastUiState.Content)

        vm.onLocationPermissionAvailable()

        assertTrue(vm.uiState.value is ForecastUiState.Content)
    }

    @Test
    fun `manual city search loads content without any location permission`() = runTest {
        val city = CityUiModel("Lisbon", "Portugal", 38.72, -9.14)
        coEvery { getForecastByCity(any(), any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()

        vm.onAction(ForecastUiAction.OnCitySelected(city))

        coVerify(exactly = 1) { getForecastByCity(any(), any()) }
        coVerify(exactly = 0) { getCurrentLocationForecast(any()) }
        assertTrue(vm.uiState.value is ForecastUiState.Content)
    }

    @Test
    fun `permission failure during load returns to first screen`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns
            AppResult.Failure(AppError.NoLocationPermission)
        val vm = viewModel()

        vm.onLocationPermissionGranted()

        assertTrue(vm.uiState.value is ForecastUiState.InitialChoice)
    }

    @Test
    fun `network failure during current location load shows error`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Failure(AppError.Network)
        val vm = viewModel()

        vm.onLocationPermissionGranted()

        assertTrue(vm.uiState.value is ForecastUiState.Error)
    }

    @Test
    fun `units change does not load anything while on the first screen`() = runTest {
        val vm = viewModel()

        settingsFlow.value = AppSettings.DEFAULT.copy(measurementSystem = MeasurementSystem.IMPERIAL)

        assertTrue(vm.uiState.value is ForecastUiState.InitialChoice)
        coVerify(exactly = 0) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `units change reloads current location once loaded`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()
        vm.onLocationPermissionGranted()

        settingsFlow.value = AppSettings.DEFAULT.copy(measurementSystem = MeasurementSystem.IMPERIAL)

        coVerify(exactly = 2) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `theme-only change does not reload`() = runTest {
        coEvery { getCurrentLocationForecast(any()) } returns AppResult.Success(domainForecast)
        val vm = viewModel()
        vm.onLocationPermissionGranted()

        settingsFlow.value = AppSettings.DEFAULT.copy(themeMode = ThemeMode.DARK)

        coVerify(exactly = 1) { getCurrentLocationForecast(any()) }
    }

    @Test
    fun `requesting permission shows requesting state`() = runTest {
        val vm = viewModel()

        vm.onLocationPermissionRequested()

        assertEquals(ForecastUiState.RequestingPermission, vm.uiState.value)
    }
}