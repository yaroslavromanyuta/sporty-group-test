package com.sportygroup.weatherapp.feature.forecast.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ForecastRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeocodingRetrofit