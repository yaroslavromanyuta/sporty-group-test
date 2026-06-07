package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection

/** Persistent store of the user's last forecast selection, restored on app launch. */
interface LastForecastSelectionLocalDataSource {

    /** Returns the saved selection, or null when none has been persisted (or it is unreadable). */
    suspend fun read(): LastForecastSelection?

    /** Persists [selection], replacing any previously saved one. Survives app restarts. */
    suspend fun save(selection: LastForecastSelection)
}
