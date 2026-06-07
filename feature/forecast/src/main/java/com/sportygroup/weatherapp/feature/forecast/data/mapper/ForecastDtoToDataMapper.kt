package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastDataModel
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto

interface ForecastDtoToDataMapper {
    fun map(dto: ForecastResponseDto): ForecastDataModel
}
