package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/** Persists the user's [selection] so it can be restored on the next app launch. */
class SaveLastForecastSelectionUseCase @Inject constructor(
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(selection: LastForecastSelection) =
        repository.saveLastSelection(selection)
}
