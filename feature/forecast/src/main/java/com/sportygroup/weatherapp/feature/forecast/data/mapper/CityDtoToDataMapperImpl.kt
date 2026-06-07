package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.CityDataModel
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResultDto
import javax.inject.Inject

class CityDtoToDataMapperImpl @Inject constructor() : CityDtoToDataMapper {
    override fun map(input: GeocodingResultDto): CityDataModel = CityDataModel(
        name = input.name,
        region = listOfNotNull(input.admin1, input.country).distinct().joinToString(", "),
        latitude = input.latitude,
        longitude = input.longitude,
    )
}
