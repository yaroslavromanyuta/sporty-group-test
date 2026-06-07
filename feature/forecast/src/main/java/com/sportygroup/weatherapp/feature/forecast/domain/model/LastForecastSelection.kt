package com.sportygroup.weatherapp.feature.forecast.domain.model

import com.sportygroup.weatherapp.core.model.City

/**
 * The forecast source the user last viewed. Persisted so the app can restore it on launch
 * instead of always showing the initial choice screen. Kept separate from recent cities
 * (search suggestions) and the forecast cache (offline fallback).
 */
sealed interface LastForecastSelection {

    /** The user was viewing the current-location forecast. */
    data object CurrentLocation : LastForecastSelection

    /** The user was viewing the forecast for an explicitly selected [city]. */
    data class ManualCity(val city: City) : LastForecastSelection
}
