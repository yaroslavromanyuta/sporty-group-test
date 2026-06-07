package com.sportygroup.weatherapp.feature.forecast.data.remote.api

import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/** Open-Meteo Geocoding API. Base URL: https://geocoding-api.open-meteo.com/ */
interface GeocodingApi {

    @GET("v1/search")
    suspend fun search(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json",
    ): GeocodingResponseDto
}