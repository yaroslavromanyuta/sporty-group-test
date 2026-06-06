package com.sportygroup.weatherapp.core.common

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateTimeProviderImpl @Inject constructor() : DateTimeProvider {
    private val clock: Clock = Clock.systemDefaultZone()
    override fun now(): LocalDateTime = LocalDateTime.now(clock)
    override fun zone(): ZoneId = clock.zone
}