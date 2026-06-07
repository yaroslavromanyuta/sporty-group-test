package com.sportygroup.weatherapp.feature.forecast.data.remote

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SafeApiCallTest {

    @Test
    fun `returns Success when block completes normally`() = runTest {
        val result = safeApiCall { "value" }
        assertEquals(AppResult.Success("value"), result)
    }

    @Test
    fun `rethrows CancellationException without wrapping`() = runTest {
        val cause = CancellationException("cancelled")
        val thrown = runCatching { safeApiCall<Unit> { throw cause } }.exceptionOrNull()
        assertSame(cause, thrown)
    }

    @Test
    fun `maps SocketTimeoutException to AppError Timeout`() = runTest {
        val result = safeApiCall<Unit> { throw SocketTimeoutException() }
        assertEquals(AppResult.Failure(AppError.Timeout), result)
    }

    @Test
    fun `maps IOException to AppError Network`() = runTest {
        val result = safeApiCall<Unit> { throw IOException() }
        assertEquals(AppResult.Failure(AppError.Network), result)
    }

    @Test
    fun `maps unknown Exception to AppError Unknown`() = runTest {
        val cause = RuntimeException("boom")
        val result = safeApiCall<Unit> { throw cause }
        assertTrue(result is AppResult.Failure)
        val error = (result as AppResult.Failure).error
        assertTrue(error is AppError.Unknown)
        assertSame(cause, (error as AppError.Unknown).cause)
    }
}
