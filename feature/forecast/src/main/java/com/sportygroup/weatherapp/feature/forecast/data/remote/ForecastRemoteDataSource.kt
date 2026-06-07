package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.ForecastApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto

/** Thin wrapper over [ForecastApi] that keeps Retrofit details out of the repository. */
interface ForecastRemoteDataSource {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        units: ForecastUnits,
    ): AppResult<ForecastResponseDto>
}