package com.sportygroup.weatherapp.feature.forecast.data.model

/**
 * Data-layer representation of a forecast: DTO lists are zipped into row objects and
 * nullable fields are coalesced, but raw weather codes and ISO time strings are kept
 * until the data->domain mapper interprets them.
 */
data class ForecastDataModel(
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val current: CurrentDataModel,
    val hourly: List<HourlyEntryDataModel>,
    val daily: List<DailyEntryDataModel>,
)

data class CurrentDataModel(
    val time: String?,
    val temperature: Double,
    val apparentTemperature: Double,
    val humidityPercent: Int,
    val weatherCode: Int,
    val windSpeed: Double,
    val pressureHpa: Double,
)

data class HourlyEntryDataModel(
    val time: String,
    val temperature: Double,
    val weatherCode: Int,
    val precipitationProbability: Int,
)

data class DailyEntryDataModel(
    val date: String,
    val weatherCode: Int,
    val high: Double,
    val low: Double,
    val precipitationProbability: Int,
)