package com.sportygroup.weatherapp.feature.forecast.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Raw Open-Meteo forecast response. Stays inside the data/remote package. */
@Serializable
data class ForecastResponseDto(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String? = null,
    val current: CurrentDto? = null,
    val hourly: HourlyDto? = null,
    val daily: DailyDto? = null,
)

@Serializable
data class CurrentDto(
    val time: String? = null,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("relative_humidity_2m") val relativeHumidity: Int? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("pressure_msl") val pressure: Double? = null,
)

@Serializable
data class HourlyDto(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperature: List<Double> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
    @SerialName("precipitation_probability") val precipitationProbability: List<Int?> = emptyList(),
)

@Serializable
data class DailyDto(
    val time: List<String> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
    @SerialName("temperature_2m_max") val temperatureMax: List<Double> = emptyList(),
    @SerialName("temperature_2m_min") val temperatureMin: List<Double> = emptyList(),
    @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?> = emptyList(),
)