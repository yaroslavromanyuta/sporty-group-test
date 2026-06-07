package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/** Searches cities by name. Blank queries short-circuit to an empty list. */
class SearchCitiesUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(query: String): AppResult<List<City>> {
        if (query.isBlank()) return AppResult.Success(emptyList())
        return repository.searchCities(query.trim())
    }
}