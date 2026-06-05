package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/** Loads the forecast for an explicitly selected city. */
class GetForecastByCityUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(city: City): AppResult<Forecast> = repository.getForecast(city)
}