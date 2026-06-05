package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.ForecastApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import javax.inject.Inject

/** Thin wrapper over [ForecastApi] that keeps Retrofit details out of the repository. */
interface ForecastRemoteDataSource {
    suspend fun getForecast(latitude: Double, longitude: Double): AppResult<ForecastResponseDto>
}

class DefaultForecastRemoteDataSource @Inject constructor(
    private val api: ForecastApi,
) : ForecastRemoteDataSource {
    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): AppResult<ForecastResponseDto> = safeApiCall {
        api.getForecast(latitude = latitude, longitude = longitude)
    }
}