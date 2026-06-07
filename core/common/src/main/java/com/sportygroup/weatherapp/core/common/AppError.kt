package com.sportygroup.weatherapp.core.common

/** Domain-level error taxonomy. Presentation maps these to user-facing messages. */
sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError
    data object NoLocationPermission : AppError
    data object LocationUnavailable : AppError
    data object EmptyResult : AppError
    /** A non-retriable HTTP error (4xx). [code] is the HTTP status code. */
    data class ServerError(val code: Int) : AppError
    data class Unknown(val cause: Throwable? = null) : AppError
}