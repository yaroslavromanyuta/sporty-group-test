package com.sportygroup.weatherapp.feature.forecast.domain

import java.time.Duration

/**
 * Cache freshness rule for forecasts. A cached forecast is considered "fresh" for
 * [FRESHNESS]; once older it is "stale". Stale cache is still usable as an offline
 * fallback — the staleness is only used to flag the data as possibly outdated in the UI.
 */
object ForecastCachePolicy {

    val FRESHNESS: Duration = Duration.ofMinutes(30)

    fun isFresh(cachedAtEpochMillis: Long, nowEpochMillis: Long): Boolean =
        nowEpochMillis - cachedAtEpochMillis <= FRESHNESS.toMillis()
}