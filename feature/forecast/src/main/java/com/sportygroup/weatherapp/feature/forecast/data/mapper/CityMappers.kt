package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.core.common.Mapper
import com.sportygroup.weatherapp.feature.forecast.data.model.CityDataModel
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.GeocodingResultDto
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import javax.inject.Inject

interface CityDtoToDataMapper : Mapper<GeocodingResultDto, CityDataModel>
interface CityDataToDomainMapper : Mapper<CityDataModel, City>

class DefaultCityDtoToDataMapper @Inject constructor() : CityDtoToDataMapper {
    override fun map(input: GeocodingResultDto): CityDataModel = CityDataModel(
        name = input.name,
        region = listOfNotNull(input.admin1, input.country).distinct().joinToString(", "),
        latitude = input.latitude,
        longitude = input.longitude,
    )
}

class DefaultCityDataToDomainMapper @Inject constructor() : CityDataToDomainMapper {
    override fun map(input: CityDataModel): City = City(
        name = input.name,
        region = input.region,
        coordinates = Coordinates(input.latitude, input.longitude),
        isCurrentLocation = false,
    )
}