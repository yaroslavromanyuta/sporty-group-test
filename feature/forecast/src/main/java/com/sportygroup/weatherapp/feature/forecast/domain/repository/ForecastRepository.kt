package com.sportygroup.weatherapp.feature.forecast.domain.repository

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast

/** Domain-facing gateway for forecast and city data. Implemented in the data layer. */
interface ForecastRepository {
    suspend fun getForecast(city: City): AppResult<Forecast>
    suspend fun getForecastByCoordinates(coordinates: Coordinates): AppResult<Forecast>
    suspend fun searchCities(query: String): AppResult<List<City>>
}