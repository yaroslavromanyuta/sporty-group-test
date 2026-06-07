package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import javax.inject.Inject

/** Loads the forecast for raw coordinates using the given unit settings. */
class GetForecastByCoordinatesUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(
        coordinates: Coordinates,
        settings: AppSettings,
    ): AppResult<Forecast> = repository.getForecastByCoordinates(coordinates, settings)
}