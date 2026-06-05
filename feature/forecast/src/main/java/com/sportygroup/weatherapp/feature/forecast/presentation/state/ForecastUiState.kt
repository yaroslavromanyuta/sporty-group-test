package com.sportygroup.weatherapp.feature.forecast.presentation.state

import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorMessage
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel

/** Top-level state for the forecast (home) screen. */
sealed interface ForecastUiState {

    /**
     * Start screen and decision point shown on launch and whenever current-location weather
     * is not (yet) available. The user must explicitly choose current location or manual
     * search; we never auto-navigate away from here.
     *
     * @param permissionDenied true after the user declined the location permission, so the
     *   screen can remind them that manual search is still available.
     * @param permissionPermanentlyDenied true when the permission can no longer be requested
     *   in-app (e.g. "Don't allow" on Android 11+); the user must enable it in Settings.
     */
    data class InitialChoice(
        val permissionDenied: Boolean = false,
        val permissionPermanentlyDenied: Boolean = false,
        val canSearchManually: Boolean = true,
    ) : ForecastUiState

    /** The system permission dialog has been launched and we are awaiting its result. */
    data object RequestingPermission : ForecastUiState

    /** Loading a forecast (by current location or by a selected city). */
    data object Loading : ForecastUiState

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