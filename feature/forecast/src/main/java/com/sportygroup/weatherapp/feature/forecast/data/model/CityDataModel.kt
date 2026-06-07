package com.sportygroup.weatherapp.feature.forecast.data.model

/** Data-layer representation of a searchable city. */
data class CityDataModel(
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
)