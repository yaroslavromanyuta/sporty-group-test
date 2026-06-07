package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Streams the user's recently selected cities (most recent first). */
class ObserveRecentCitiesUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    operator fun invoke(): Flow<List<City>> = repository.observeRecentCities()
}