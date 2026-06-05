package com.sportygroup.weatherapp.feature.forecast.presentation.state

import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.model.TemperatureUnit

/** User intents originating from the forecast/home screen. */
sealed interface ForecastUiAction {
    data object OnRetryClick : ForecastUiAction
    data object OnRefresh : ForecastUiAction
    data object OnUseCurrentLocationClick : ForecastUiAction
    data class OnUnitChange(val unit: TemperatureUnit) : ForecastUiAction
    data class OnPermissionResult(val granted: Boolean) : ForecastUiAction
    data class OnCitySelected(val city: CityUiModel) : ForecastUiAction
}

/** User intents originating from the city search screen. */
sealed interface CitySearchUiAction {
    data class OnQueryChanged(val query: String) : CitySearchUiAction
    data object OnClearQuery : CitySearchUiAction
    data class OnCitySelected(val city: CityUiModel) : CitySearchUiAction
    data object OnUseCurrentLocation : CitySearchUiAction
}