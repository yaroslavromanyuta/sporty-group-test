package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/** Runs [block], converting thrown technical exceptions into typed [AppError]s. */
suspend inline fun <T> safeApiCall(block: () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: SocketTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (e: IOException) {
    AppResult.Failure(AppError.Network)
} catch (e: HttpException) {
    if (e.code() in 500..599) AppResult.Failure(AppError.Network)
    else AppResult.Failure(AppError.ServerError(e.code()))
} catch (e: Exception) {
    AppResult.Failure(AppError.Unknown(e))
}