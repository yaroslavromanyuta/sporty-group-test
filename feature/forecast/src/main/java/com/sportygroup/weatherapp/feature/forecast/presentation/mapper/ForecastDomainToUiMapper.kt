package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.presentation.model.ForecastUiModel
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem

interface ForecastDomainToUiMapper {
    fun map(forecast: Forecast, measurementSystem: MeasurementSystem): ForecastUiModel
}
