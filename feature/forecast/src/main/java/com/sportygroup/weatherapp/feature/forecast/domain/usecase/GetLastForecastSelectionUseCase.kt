package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/** Reads the user's last forecast selection, or null when none has been saved yet. */
class GetLastForecastSelectionUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(): LastForecastSelection? = repository.getLastSelection()
}
