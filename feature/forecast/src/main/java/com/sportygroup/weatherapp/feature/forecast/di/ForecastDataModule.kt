package com.sportygroup.weatherapp.feature.forecast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastCacheMapper
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastCacheMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastLocalDataSourceImpl
import com.sportygroup.weatherapp.feature.forecast.data.local.RecentCitiesLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.local.RecentCitiesLocalDataSourceImpl
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
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.forecastDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "forecast_cache",
)

@Module
@InstallIn(SingletonComponent::class)
abstract class ForecastDataModule {

    @Binds
    @Singleton
    abstract fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    abstract fun bindForecastLocalDataSource(
        impl: ForecastLocalDataSourceImpl,
    ): ForecastLocalDataSource

    @Binds
    abstract fun bindRecentCitiesLocalDataSource(
        impl: RecentCitiesLocalDataSourceImpl,
    ): RecentCitiesLocalDataSource

    @Binds
    abstract fun bindForecastCacheMapper(impl: ForecastCacheMapperImpl): ForecastCacheMapper

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

    companion object {
        @Provides
        @Singleton
        @ForecastCacheDataStore
        fun provideForecastDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.forecastDataStore
    }
}