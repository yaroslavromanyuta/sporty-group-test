package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection

/** Converts the last forecast selection between its domain and storage representations. */
interface LastForecastSelectionMapper {
    fun toData(selection: LastForecastSelection): LastForecastSelectionDataModel
    fun toDomain(model: LastForecastSelectionDataModel): LastForecastSelection
}
