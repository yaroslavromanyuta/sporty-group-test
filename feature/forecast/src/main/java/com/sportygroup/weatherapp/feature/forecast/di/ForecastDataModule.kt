package com.sportygroup.weatherapp.feature.forecast.di

import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSourceImpl
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSourceImpl
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
        impl: ForecastRemoteDataSourceImpl,
    ): ForecastRemoteDataSource

    @Binds
    abstract fun bindCitySearchRemoteDataSource(
        impl: CitySearchRemoteDataSourceImpl,
    ): CitySearchRemoteDataSource

    @Binds
    abstract fun bindForecastDtoToDataMapper(
        impl: ForecastDtoToDataMapperImpl,
    ): ForecastDtoToDataMapper

    @Binds
    abstract fun bindForecastDataToDomainMapper(
        impl: ForecastDataToDomainMapperImpl,
    ): ForecastDataToDomainMapper

    @Binds
    abstract fun bindCityDtoToDataMapper(impl: CityDtoToDataMapperImpl): CityDtoToDataMapper

    @Binds
    abstract fun bindCityDataToDomainMapper(
        impl: CityDataToDomainMapperImpl,
    ): CityDataToDomainMapper
}