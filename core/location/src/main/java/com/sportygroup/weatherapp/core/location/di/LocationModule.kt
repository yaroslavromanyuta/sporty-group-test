package com.sportygroup.weatherapp.core.location.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sportygroup.weatherapp.core.location.CurrentCityNameResolver
import com.sportygroup.weatherapp.core.location.CurrentCityNameResolverImpl
import com.sportygroup.weatherapp.core.location.CurrentLocationProvider
import com.sportygroup.weatherapp.core.location.CurrentLocationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(impl: CurrentLocationProviderImpl): CurrentLocationProvider

    @Binds
    @Singleton
    abstract fun bindCityNameResolver(impl: CurrentCityNameResolverImpl): CurrentCityNameResolver

    companion object {
        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context,
        ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }
}