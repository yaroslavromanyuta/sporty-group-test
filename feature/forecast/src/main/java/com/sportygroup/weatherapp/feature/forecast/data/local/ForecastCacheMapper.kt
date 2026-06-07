package com.sportygroup.weatherapp.feature.forecast.data.local

/** Maps between the domain [com.sportygroup.weatherapp.core.model.Forecast] and its cached form. */
interface ForecastCacheMapper {
    fun toCache(
        forecast: com.sportygroup.weatherapp.core.model.Forecast,
        cachedAtEpochMillis: Long,
    ): CachedForecastDataModel

    /** Rebuilds a domain forecast, tagging it as cache-sourced with the given staleness. */
    fun toDomain(
        model: CachedForecastDataModel,
        isStale: Boolean,
    ): com.sportygroup.weatherapp.core.model.Forecast
}