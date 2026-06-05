package com.sportygroup.weatherapp.core.location

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates

/** Resolves a display name (city + region) for raw coordinates. Best-effort. */
interface CurrentCityNameResolver {
    /** Returns a [City] for the coordinates, or null if reverse geocoding fails. */
    suspend fun resolve(coordinates: Coordinates): City?
}