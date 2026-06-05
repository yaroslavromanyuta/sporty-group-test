package com.sportygroup.weatherapp.feature.forecast.presentation.state

/** One-time effects emitted by the ViewModel. */
sealed interface ForecastUiEvent {
    data class ShowMessage(val message: String) : ForecastUiEvent
    data object NavigateToHome : ForecastUiEvent
    data object RequestLocationPermission : ForecastUiEvent
}