package com.sportygroup.weatherapp.feature.forecast.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ForecastRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeocodingRetrofit

/** Forecast-scoped DataStore (separate file from app settings) for caches and recent cities. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ForecastCacheDataStore