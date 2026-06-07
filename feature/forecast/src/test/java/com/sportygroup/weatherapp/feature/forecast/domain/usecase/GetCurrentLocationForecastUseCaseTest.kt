package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.location.CurrentCityNameResolver
import com.sportygroup.weatherapp.core.location.CurrentLocationProvider
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.CurrentWeather
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.core.model.WeatherCondition
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class GetCurrentLocationForecastUseCaseTest {

    private val locationProvider = mockk<CurrentLocationProvider>()
    private val cityNameResolver = mockk<CurrentCityNameResolver>()
    private val repository = mockk<ForecastRepository>()
    private val useCase = GetCurrentLocationForecastUseCase(locationProvider, cityNameResolver, repository)

    private val coordinates = Coordinates(36.7, -4.4)

    private fun forecast(city: City) = Forecast(
        city = city,
        current = CurrentWeather(24.0, 26.0, WeatherCondition.CLEAR_DAY, 50, 10.0, 1010.0, 26.0, 17.0, LocalDateTime.now()),
        hourly = emptyList(),
        daily = emptyList(),
    )

    @Test
    fun `returns NoLocationPermission when permission missing`() = runTest {
        every { locationProvider.hasLocationPermission() } returns false

        val result = useCase(AppSettings.DEFAULT)

        assertEquals(AppResult.Failure(AppError.NoLocationPermission), result)
    }

    @Test
    fun `attaches resolved city name on success`() = runTest {
        every { locationProvider.hasLocationPermission() } returns true
        coEvery { locationProvider.getCurrentCoordinates() } returns AppResult.Success(coordinates)
        coEvery { repository.getForecastByCoordinates(coordinates, any()) } returns
            AppResult.Success(forecast(City("Current location", "", coordinates, true)))
        coEvery { cityNameResolver.resolve(coordinates) } returns
            City("Malaga", "Spain", coordinates, true)

        val result = useCase(AppSettings.DEFAULT)

        assertTrue(result is AppResult.Success)
        val city = (result as AppResult.Success).value.city
        assertEquals("Malaga", city.name)
        assertTrue(city.isCurrentLocation)
    }

    @Test
    fun `falls back to forecast city when reverse geocoding fails`() = runTest {
        every { locationProvider.hasLocationPermission() } returns true
        coEvery { locationProvider.getCurrentCoordinates() } returns AppResult.Success(coordinates)
        coEvery { repository.getForecastByCoordinates(coordinates, any()) } returns
            AppResult.Success(forecast(City("Current location", "", coordinates, true)))
        coEvery { cityNameResolver.resolve(coordinates) } returns null

        val result = useCase(AppSettings.DEFAULT)

        assertTrue(result is AppResult.Success)
        assertEquals("Current location", (result as AppResult.Success).value.city.name)
    }
}