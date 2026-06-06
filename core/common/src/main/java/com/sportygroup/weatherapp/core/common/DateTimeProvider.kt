package com.sportygroup.weatherapp.core.common

import java.time.LocalDateTime
import java.time.ZoneId

/** Abstraction over "now" so presentation mapping and tests stay deterministic. */
interface DateTimeProvider {
    fun now(): LocalDateTime
    fun zone(): ZoneId
}