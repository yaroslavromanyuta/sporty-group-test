package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.StringResources
import com.sportygroup.weatherapp.feature.forecast.R
import javax.inject.Inject

/** User-facing presentation of an [AppError]. */
data class ErrorMessage(val title: String, val message: String, val code: String)

/** Maps technical [AppError]s to friendly, actionable messages. */
class ErrorUiMapper @Inject constructor(
    private val stringResources: StringResources,
) {

    fun map(error: AppError): ErrorMessage = when (error) {
        AppError.Network -> ErrorMessage(
            title = stringResources.getString(R.string.error_generic_title),
            message = stringResources.getString(R.string.error_network_message),
            code = "NETWORK",
        )
        AppError.Timeout -> ErrorMessage(
            title = stringResources.getString(R.string.error_timeout_title),
            message = stringResources.getString(R.string.error_timeout_message),
            code = "NETWORK_TIMEOUT",
        )
        AppError.LocationUnavailable -> ErrorMessage(
            title = stringResources.getString(R.string.error_location_unavailable_title),
            message = stringResources.getString(R.string.error_location_unavailable_message),
            code = "LOCATION_UNAVAILABLE",
        )
        AppError.NoLocationPermission -> ErrorMessage(
            title = stringResources.getString(R.string.error_no_permission_title),
            message = stringResources.getString(R.string.error_no_permission_message),
            code = "NO_PERMISSION",
        )
        AppError.EmptyResult -> ErrorMessage(
            title = stringResources.getString(R.string.error_empty_title),
            message = stringResources.getString(R.string.error_empty_message),
            code = "EMPTY",
        )
        is AppError.Unknown -> ErrorMessage(
            title = stringResources.getString(R.string.error_generic_title),
            message = stringResources.getString(R.string.error_unknown_message),
            code = "UNKNOWN",
        )
    }
}