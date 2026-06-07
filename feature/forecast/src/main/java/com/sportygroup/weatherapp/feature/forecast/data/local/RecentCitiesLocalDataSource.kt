package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import kotlinx.coroutines.flow.Flow

/** Persistent store of the user's recently selected (manually searched) cities. */
interface RecentCitiesLocalDataSource {

    /** Streams recent cities, most recent first. */
    fun observe(): Flow<List<City>>

    /**
     * Adds [city] to the top, moving an existing duplicate up, capping the list and dropping
     * current-location entries. Persists across app restarts.
     */
    suspend fun add(city: City)
}