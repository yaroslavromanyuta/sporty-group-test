package com.sportygroup.weatherapp.feature.forecast.data.local

import kotlinx.serialization.Serializable

/**
 * Serializable, storage-friendly representation of the user's last forecast selection.
 * For [Type.MANUAL_CITY] the chosen [city] is present; for [Type.CURRENT_LOCATION] it is null.
 * Lives in data/local and never leaves the data layer.
 */
@Serializable
data class LastForecastSelectionDataModel(
    val type: Type,
    val city: CachedCityDataModel? = null,
) {
    enum class Type { CURRENT_LOCATION, MANUAL_CITY }
}
