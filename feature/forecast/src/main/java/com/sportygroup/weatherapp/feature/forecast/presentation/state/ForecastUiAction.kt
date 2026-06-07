package com.sportygroup.weatherapp.feature.forecast.presentation.state

import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel

/**
 * User intents originating from the forecast/home screen.
 *
 * Note: tapping "Use current location" is handled via an explicit callback in the route
 * (so Android permission APIs stay out of the ViewModel), not as a [ForecastUiAction].
 */
sealed interface ForecastUiAction {
    data object OnRetryClick : ForecastUiAction
    data object OnRefresh : ForecastUiAction
    data class OnCitySelected(val city: CityUiModel) : ForecastUiAction
}

/** User intents originating from the city search screen. */
sealed interface CitySearchUiAction {
    data class OnQueryChanged(val query: String) : CitySearchUiAction
    data object OnClearQuery : CitySearchUiAction
    data class OnCitySelected(val city: CityUiModel) : CitySearchUiAction
    data object OnUseCurrentLocation : CitySearchUiAction
}