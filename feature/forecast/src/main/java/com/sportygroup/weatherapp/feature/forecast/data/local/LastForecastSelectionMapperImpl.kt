package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.data.local.LastForecastSelectionDataModel.Type
import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import javax.inject.Inject

class LastForecastSelectionMapperImpl @Inject constructor() : LastForecastSelectionMapper {

    override fun toData(selection: LastForecastSelection): LastForecastSelectionDataModel =
        when (selection) {
            LastForecastSelection.CurrentLocation ->
                LastForecastSelectionDataModel(type = Type.CURRENT_LOCATION)
            is LastForecastSelection.ManualCity ->
                LastForecastSelectionDataModel(type = Type.MANUAL_CITY, city = selection.city.toCached())
        }

    override fun toDomain(model: LastForecastSelectionDataModel): LastForecastSelection =
        when (model.type) {
            Type.CURRENT_LOCATION -> LastForecastSelection.CurrentLocation
            Type.MANUAL_CITY -> LastForecastSelection.ManualCity(
                // A manual selection always carries a city; fall back to current location if the
                // stored payload is somehow incomplete rather than crashing on a corrupt entry.
                city = model.city?.toDomain()
                    ?: return LastForecastSelection.CurrentLocation,
            )
        }

    private fun CachedCityDataModel.toDomain(): City = City(
        name = name,
        region = region,
        coordinates = Coordinates(latitude, longitude),
        isCurrentLocation = false,
    )

    private fun City.toCached(): CachedCityDataModel = CachedCityDataModel(
        name = name,
        region = region,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        isCurrentLocation = false,
    )
}
