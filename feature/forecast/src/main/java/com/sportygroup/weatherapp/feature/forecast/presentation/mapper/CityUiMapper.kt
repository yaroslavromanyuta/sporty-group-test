package com.sportygroup.weatherapp.feature.forecast.presentation.mapper

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import javax.inject.Inject

/** Bridges domain [City] and presentation [CityUiModel] in both directions. */
class CityUiMapper @Inject constructor() {

    fun toUi(city: City): CityUiModel = CityUiModel(
        name = city.name,
        region = city.region,
        latitude = city.coordinates.latitude,
        longitude = city.coordinates.longitude,
        isCurrentLocation = city.isCurrentLocation,
    )

    fun toDomain(model: CityUiModel): City = City(
        name = model.name,
        region = model.region,
        coordinates = Coordinates(model.latitude, model.longitude),
        isCurrentLocation = model.isCurrentLocation,
    )
}