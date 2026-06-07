package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.Forecast

/** Local persistence for the last successful forecast per [ForecastCacheKey]. */
interface ForecastLocalDataSource {

    /** Stores [forecast] under [key], stamping it with the current time. */
    suspend fun save(key: ForecastCacheKey, forecast: Forecast)

    /**
     * Returns the cached forecast for [key], or null if absent/unreadable. The result is tagged
     * as [com.sportygroup.weatherapp.core.model.ForecastSource.CACHE] with a staleness flag based
     * on the freshness policy. Stale entries are still returned (offline fallback).
     */
    suspend fun read(key: ForecastCacheKey): Forecast?
}