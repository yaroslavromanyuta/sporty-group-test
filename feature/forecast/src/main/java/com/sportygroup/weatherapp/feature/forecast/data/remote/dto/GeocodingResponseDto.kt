package com.sportygroup.weatherapp.feature.forecast.data.remote.dto

import kotlinx.serialization.Serializable

/** Raw Open-Meteo geocoding response. `results` is absent when there are no matches. */
@Serializable
data class GeocodingResponseDto(
    val results: List<GeocodingResultDto>? = null,
)

@Serializable
data class GeocodingResultDto(
    val id: Long = 0,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val country: String? = null,
    val admin1: String? = null,
)