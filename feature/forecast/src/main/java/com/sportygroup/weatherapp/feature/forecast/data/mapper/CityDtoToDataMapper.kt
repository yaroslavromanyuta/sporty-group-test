package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.core.common.Mapper
import com.sportygroup.weatherapp.feature.forecast.data.model.CityDataModel
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResultDto

interface CityDtoToDataMapper : Mapper<GeocodingResultDto, CityDataModel>
