package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastUnits
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.ForecastApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import javax.inject.Inject

class ForecastRemoteDataSourceImpl @Inject constructor(
    private val api: ForecastApi,
) : ForecastRemoteDataSource {
    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        units: ForecastUnits,
    ): AppResult<ForecastResponseDto> = safeApiCall {
        api.getForecast(
            latitude = latitude,
            longitude = longitude,
            temperatureUnit = units.temperatureUnit,
            windSpeedUnit = units.windSpeedUnit,
            precipitationUnit = units.precipitationUnit,
        )
    }
}
