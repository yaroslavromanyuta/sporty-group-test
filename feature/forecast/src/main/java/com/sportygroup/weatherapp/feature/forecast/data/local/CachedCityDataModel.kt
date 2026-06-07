package com.sportygroup.weatherapp.feature.forecast.data.local

import kotlinx.serialization.Serializable

/**
 * Serializable, storage-friendly representation of a city. Used both inside a cached forecast
 * and for the persisted "recent cities" list. Lives in data/local and never leaves the data layer.
 */
@Serializable
data class CachedCityDataModel(
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean = false,
)