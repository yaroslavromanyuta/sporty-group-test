package com.sportygroup.weatherapp.feature.forecast.data.remote.api

import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/** Open-Meteo Forecast API. Base URL: https://api.open-meteo.com/ */
interface ForecastApi {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
        @Query("precipitation_unit") precipitationUnit: String = "mm",
        @Query("current") current: String = CURRENT_FIELDS,
        @Query("hourly") hourly: String = HOURLY_FIELDS,
        @Query("daily") daily: String = DAILY_FIELDS,
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timezone") timezone: String = "auto",
    ): ForecastResponseDto

    companion object {
        const val CURRENT_FIELDS =
            "temperature_2m,apparent_temperature,relative_humidity_2m,weather_code,wind_speed_10m,pressure_msl"
        const val HOURLY_FIELDS =
            "temperature_2m,weather_code,precipitation_probability,wind_speed_10m"
        const val DAILY_FIELDS =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max"
    }
}