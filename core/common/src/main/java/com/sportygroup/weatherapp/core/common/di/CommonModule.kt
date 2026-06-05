package com.sportygroup.weatherapp.core.common.di

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.common.DefaultDispatcherProvider
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.common.SystemDateTimeProvider
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
    abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindDateTimeProvider(impl: SystemDateTimeProvider): DateTimeProvider
}