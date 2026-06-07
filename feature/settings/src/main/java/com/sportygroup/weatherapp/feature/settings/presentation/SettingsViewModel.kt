package com.sportygroup.weatherapp.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportygroup.weatherapp.feature.settings.presentation.mapper.SettingsUiMapper
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiAction
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiState
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.usecase.ObserveSettingsUseCase
import com.sportygroup.weatherapp.lib.settings.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettings: ObserveSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase,
    private val uiMapper: SettingsUiMapper,
) : ViewModel() {

    private var currentSettings: AppSettings = AppSettings.DEFAULT

    private val _uiState = MutableStateFlow(SettingsUiState(uiMapper.toUi(currentSettings)))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSettings().collect { settings ->
                currentSettings = settings
                _uiState.value = SettingsUiState(uiMapper.toUi(settings))
            }
        }
    }

    fun onAction(action: SettingsUiAction) {
        val updated = when (action) {
            is SettingsUiAction.SelectMeasurementSystem ->
                currentSettings.copy(measurementSystem = action.value)
            is SettingsUiAction.SelectTemperatureUnit ->
                currentSettings.copy(temperatureUnit = action.value)
            is SettingsUiAction.SelectThemeMode ->
                currentSettings.copy(themeMode = action.value)
        }
        if (updated == currentSettings) return
        viewModelScope.launch { updateSettings(updated) }
    }
}