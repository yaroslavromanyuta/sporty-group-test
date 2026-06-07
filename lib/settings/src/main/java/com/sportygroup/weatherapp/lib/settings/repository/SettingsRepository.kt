package com.sportygroup.weatherapp.lib.settings.repository

import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

/** Public contract for reading and persisting [AppSettings]. Implemented in :feature:settings. */
interface SettingsRepository {
    fun observe(): Flow<AppSettings>
    suspend fun update(settings: AppSettings)
}