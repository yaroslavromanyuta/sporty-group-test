package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchCitiesUseCaseTest {

    private val repository = mockk<ForecastRepository>()
    private val useCase = SearchCitiesUseCase(repository)

    @Test
    fun `blank query returns empty without touching repository`() = runTest {
        val result = useCase("   ")

        assertTrue(result is AppResult.Success)
        assertEquals(emptyList<City>(), (result as AppResult.Success).value)
        coVerify(exactly = 0) { repository.searchCities(any()) }
    }

    @Test
    fun `delegates trimmed query to repository`() = runTest {
        val city = City("Malaga", "Spain", Coordinates(36.7, -4.4))
        coEvery { repository.searchCities("Malaga") } returns AppResult.Success(listOf(city))

        val result = useCase("  Malaga  ")

        assertEquals(AppResult.Success(listOf(city)), result)
        coVerify(exactly = 1) { repository.searchCities("Malaga") }
    }
}