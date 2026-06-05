package com.sportygroup.weatherapp.feature.forecast.di

import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultCityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultCityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.DefaultCitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.DefaultForecastRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.repository.ForecastRepositoryImpl
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ForecastDataModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    abstract fun bindForecastRemoteDataSource(
        impl: DefaultForecastRemoteDataSource,
    ): ForecastRemoteDataSource

    @Binds
    abstract fun bindCitySearchRemoteDataSource(
        impl: DefaultCitySearchRemoteDataSource,
    ): CitySearchRemoteDataSource

    @Binds
    abstract fun bindForecastDtoToDataMapper(
        impl: DefaultForecastDtoToDataMapper,
    ): ForecastDtoToDataMapper

    @Binds
    abstract fun bindForecastDataToDomainMapper(
        impl: DefaultForecastDataToDomainMapper,
    ): ForecastDataToDomainMapper

    @Binds
    abstract fun bindCityDtoToDataMapper(impl: DefaultCityDtoToDataMapper): CityDtoToDataMapper

    @Binds
    abstract fun bindCityDataToDomainMapper(
        impl: DefaultCityDataToDomainMapper,
    ): CityDataToDomainMapper
}