package com.sportygroup.weatherapp.core.common

import kotlinx.coroutines.CoroutineDispatcher

/** Abstraction over coroutine dispatchers so tests can inject deterministic ones. */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}