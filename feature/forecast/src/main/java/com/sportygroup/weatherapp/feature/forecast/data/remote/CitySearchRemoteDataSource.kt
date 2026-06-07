package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.GeocodingApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResponseDto

/** Thin wrapper over [GeocodingApi]. */
interface CitySearchRemoteDataSource {
    suspend fun search(query: String): AppResult<GeocodingResponseDto>
}