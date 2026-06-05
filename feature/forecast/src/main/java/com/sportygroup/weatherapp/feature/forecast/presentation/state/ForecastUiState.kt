package com.sportygroup.weatherapp.feature.forecast.presentation.state

import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorMessage
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel

/** Top-level state for the forecast (home) screen. */
sealed interface ForecastUiState {

    data object Loading : ForecastUiState

    data class PermissionRequired(
        val canSearchManually: Boolean = true,
    ) : ForecastUiState

    data class Content(
        val forecast: ForecastUiModel,
        val isRefreshing: Boolean = false,
    ) : ForecastUiState

    data class Error(
        val error: ErrorMessage,
        val canRetry: Boolean = true,
        val canSearchAnotherCity: Boolean = true,
    ) : ForecastUiState
}