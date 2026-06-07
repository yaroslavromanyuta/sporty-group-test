package com.sportygroup.weatherapp.feature.forecast.di

import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ForecastDomainToUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ForecastDomainToUiMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ForecastPresentationModule {

    @Binds
    abstract fun bindForecastDomainToUiMapper(
        impl: ForecastDomainToUiMapperImpl,
    ): ForecastDomainToUiMapper
}