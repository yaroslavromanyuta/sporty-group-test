package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/**
 * Records a manually selected [city] as a recent search. Current-location entries are
 * ignored so the recent list only ever contains explicitly searched cities.
 */
class AddRecentCityUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(city: City) {
        if (city.isCurrentLocation) return
        repository.addRecentCity(city)
    }
}