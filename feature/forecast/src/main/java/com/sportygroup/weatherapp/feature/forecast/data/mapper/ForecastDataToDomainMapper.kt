package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastDataModel
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast

interface ForecastDataToDomainMapper {
    /** Combines parsed weather data with the resolved [city] into a domain [Forecast]. */
    fun map(model: ForecastDataModel, city: City): Forecast
}