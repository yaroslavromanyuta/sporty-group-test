package com.sportygroup.weatherapp.feature.forecast.presentation

import app.cash.turbine.test
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
import com.sportygroup.weatherapp.feature.forecast.presentation.model.TemperatureUnit
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private val getCurrentLocationForecast = mockk<GetCurrentLocationForecastUseCase>()
    private val getForecastByCity = mockk<GetForecastByCityUseCase>()
    private val searchCities = mockk<SearchCitiesUseCase>()
    private val forecastUiMapper = mockk<ForecastDomainToUiMapper>()
    private val cityUiMapper = CityUiMapper()
    private val errorUiMapper = ErrorUiMapper()

    private val domainForecast = Forecast(
        city = City("Malaga", "Spain", Coordinates(36.7, -4.4), isCurrentLocation = true),
        current = mockk(relaxed = true),
        hourly = emptyList(),
        daily = emptyList(),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { forecastUiMapper.map(any(), any()) } returns ForecastPreviewData.forecast
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = ForecastViewModel(
        getCurrentLocationForecast,
        getForecastByCity,
        searchCities,
        forecastUiMapper,
        cityUiMapper,
        errorUiMapper,
    )

    @Test
    fun `successful location load emits content`() = runTest {
        coEvery { getCurrentLocationForecast() } returns AppResult.Success(domainForecast)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.Content)
    }

    @Test
    fun `missing permission emits permission required`() = runTest {
        coEvery { getCurrentLocationForecast() } returns AppResult.Failure(AppError.NoLocationPermission)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.PermissionRequired)
    }

    @Test
    fun `network failure emits error`() = runTest {
        coEvery { getCurrentLocationForecast() } returns AppResult.Failure(AppError.Network)

        val vm = viewModel()

        assertTrue(vm.uiState.value is ForecastUiState.Error)
    }

    @Test
    fun `unit change re-maps content with new unit`() = runTest {
        coEvery { getCurrentLocationForecast() } returns AppResult.Success(domainForecast)
        val vm = viewModel()

        vm.uiState.test {
            assertEquals(TemperatureUnit.CELSIUS, (awaitItem() as ForecastUiState.Content).unit)
            vm.onAction(ForecastUiAction.OnUnitChange(TemperatureUnit.FAHRENHEIT))
            assertEquals(TemperatureUnit.FAHRENHEIT, (awaitItem() as ForecastUiState.Content).unit)
        }
    }
}