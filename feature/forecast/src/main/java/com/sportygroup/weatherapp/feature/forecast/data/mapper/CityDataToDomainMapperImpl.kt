package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.CityDataModel
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import javax.inject.Inject

class CityDataToDomainMapperImpl @Inject constructor() : CityDataToDomainMapper {
    override fun map(input: CityDataModel): City = City(
        name = input.name,
        region = input.region,
        coordinates = Coordinates(input.latitude, input.longitude),
        isCurrentLocation = false,
    )
}
