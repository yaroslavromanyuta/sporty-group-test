package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/** Loads the forecast for raw coordinates (e.g. from device location). */
class GetForecastByCoordinatesUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(coordinates: Coordinates): AppResult<Forecast> =
        repository.getForecastByCoordinates(coordinates)
}