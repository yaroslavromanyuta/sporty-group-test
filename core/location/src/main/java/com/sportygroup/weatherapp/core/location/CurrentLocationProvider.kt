package com.sportygroup.weatherapp.core.location

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.Coordinates

/** Permission-aware access to the device's current coordinates. */
interface CurrentLocationProvider {
    /** True when the app currently holds a fine or coarse location permission. */
    fun hasLocationPermission(): Boolean

    /** Returns the last/current coordinates, or a typed failure when unavailable. */
    suspend fun getCurrentCoordinates(): AppResult<Coordinates>
}