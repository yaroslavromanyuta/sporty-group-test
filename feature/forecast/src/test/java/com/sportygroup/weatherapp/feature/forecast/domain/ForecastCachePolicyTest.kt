package com.sportygroup.weatherapp.feature.forecast.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForecastCachePolicyTest {

    private val now = 1_000_000_000L
    private val freshnessMs = ForecastCachePolicy.FRESHNESS.toMillis()

    @Test
    fun `just-written cache is fresh`() {
        assertTrue(ForecastCachePolicy.isFresh(cachedAtEpochMillis = now, nowEpochMillis = now))
    }

    @Test
    fun `cache exactly at the freshness boundary is still fresh`() {
        assertTrue(ForecastCachePolicy.isFresh(now - freshnessMs, now))
    }

    @Test
    fun `cache older than the freshness window is stale`() {
        assertFalse(ForecastCachePolicy.isFresh(now - freshnessMs - 1, now))
    }
}