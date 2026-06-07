package com.sportygroup.weatherapp.core.common.di

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.common.DateTimeProviderImpl
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.common.DispatcherProviderImpl
import com.sportygroup.weatherapp.core.common.StringResources
import com.sportygroup.weatherapp.core.common.StringResourcesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(impl: DispatcherProviderImpl): DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindDateTimeProvider(impl: DateTimeProviderImpl): DateTimeProvider

    @Binds
    @Singleton
    abstract fun bindStringResources(impl: StringResourcesImpl): StringResources
}