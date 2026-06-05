package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.AppError
import javax.inject.Inject

/** User-facing presentation of an [AppError]. */
data class ErrorMessage(val title: String, val message: String, val code: String)

/** Maps technical [AppError]s to friendly, actionable messages. */
class ErrorUiMapper @Inject constructor() {

    fun map(error: AppError): ErrorMessage = when (error) {
        AppError.Network -> ErrorMessage(
            title = "Something went wrong",
            message = "We couldn't reach the weather service. Check your connection and try again.",
            code = "NETWORK",
        )
        AppError.Timeout -> ErrorMessage(
            title = "Taking too long",
            message = "The weather service didn't respond in time. Please try again.",
            code = "NETWORK_TIMEOUT",
        )
        AppError.LocationUnavailable -> ErrorMessage(
            title = "Location unavailable",
            message = "We couldn't determine your location. Try again or search for a city.",
            code = "LOCATION_UNAVAILABLE",
        )
        AppError.NoLocationPermission -> ErrorMessage(
            title = "Location permission needed",
            message = "Grant location access or search for a city to see its forecast.",
            code = "NO_PERMISSION",
        )
        AppError.EmptyResult -> ErrorMessage(
            title = "Nothing to show",
            message = "We couldn't find any forecast data. Try another city.",
            code = "EMPTY",
        )
        is AppError.Unknown -> ErrorMessage(
            title = "Something went wrong",
            message = "An unexpected error occurred. Please try again.",
            code = "UNKNOWN",
        )
    }
}