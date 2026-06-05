package com.sportygroup.weatherapp.feature.forecast.testutil

import com.sportygroup.weatherapp.core.common.DateTimeProvider
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.time.LocalDateTime
import java.time.ZoneId

/** Deterministic [DateTimeProvider] for tests. */
class FakeDateTimeProvider(
    private val now: LocalDateTime = LocalDateTime.of(2026, 6, 5, 12, 0),
) : DateTimeProvider {
    override fun now(): LocalDateTime = now
    override fun zone(): ZoneId = ZoneId.of("UTC")
}

/** [DispatcherProvider] backed by a single test dispatcher. */
class TestDispatcherProvider(
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}