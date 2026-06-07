package com.sportygroup.weatherapp.feature.settings.domain

import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import com.sportygroup.weatherapp.lib.settings.repository.SettingsRepository
import com.sportygroup.weatherapp.lib.settings.usecase.ObserveSettingsUseCase
import com.sportygroup.weatherapp.lib.settings.usecase.UpdateSettingsUseCase
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsUseCasesTest {

    private val repository = mockk<SettingsRepository>(relaxed = true)

    @Test
    fun `observe delegates to repository`() = runTest {
        val settings = AppSettings(themeMode = ThemeMode.DARK)
        every { repository.observe() } returns flowOf(settings)

        val result = ObserveSettingsUseCase(repository)().first()

        assertEquals(settings, result)
    }

    @Test
    fun `update delegates to repository`() = runTest {
        val settings = AppSettings(themeMode = ThemeMode.LIGHT)
        coEvery { repository.update(settings) } returns Unit

        UpdateSettingsUseCase(repository)(settings)

        coVerify(exactly = 1) { repository.update(settings) }
    }
}