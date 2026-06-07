package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import javax.inject.Inject

/** Loads the forecast for an explicitly selected city using the given unit settings. */
class GetForecastByCityUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(city: City, settings: AppSettings): AppResult<Forecast> =
        repository.getForecast(city, settings)
}