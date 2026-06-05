package com.sportygroup.weatherapp.core.common

/** Domain-level error taxonomy. Presentation maps these to user-facing messages. */
sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError
    data object NoLocationPermission : AppError
    data object LocationUnavailable : AppError
    data object EmptyResult : AppError
    data class Unknown(val cause: Throwable? = null) : AppError
}