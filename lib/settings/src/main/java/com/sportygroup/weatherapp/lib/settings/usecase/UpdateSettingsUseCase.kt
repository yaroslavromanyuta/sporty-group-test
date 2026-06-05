package com.sportygroup.weatherapp.lib.settings.usecase

import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.repository.SettingsRepository
import javax.inject.Inject

/** Persists a new [AppSettings] value. */
class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(settings: AppSettings) = repository.update(settings)
}