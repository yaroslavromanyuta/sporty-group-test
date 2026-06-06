package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.GeocodingApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResponseDto
import javax.inject.Inject

class CitySearchRemoteDataSourceImpl @Inject constructor(
    private val api: GeocodingApi,
) : CitySearchRemoteDataSource {
    override suspend fun search(query: String): AppResult<GeocodingResponseDto> = safeApiCall {
        api.search(name = query)
    }
}
