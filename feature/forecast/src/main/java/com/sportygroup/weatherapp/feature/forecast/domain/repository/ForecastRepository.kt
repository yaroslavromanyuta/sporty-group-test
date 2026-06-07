package com.sportygroup.weatherapp.feature.forecast.domain.repository

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

/** Domain-facing gateway for forecast and city data. Implemented in the data layer. */
interface ForecastRepository {
    suspend fun getForecast(city: City, settings: AppSettings): AppResult<Forecast>
    suspend fun getForecastByCoordinates(
        coordinates: Coordinates,
        settings: AppSettings,
    ): AppResult<Forecast>
    suspend fun searchCities(query: String): AppResult<List<City>>

    /** Streams recently selected cities, most recent first. */
    fun observeRecentCities(): Flow<List<City>>

    /** Persists [city] as the most recent search, deduplicating and capping the list. */
    suspend fun addRecentCity(city: City)
}