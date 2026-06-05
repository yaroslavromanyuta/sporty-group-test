package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.GetCurrentLocationForecastUseCase
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.GetForecastByCityUseCase
import com.sportygroup.weatherapp.feature.forecast.domain.usecase.SearchCitiesUseCase
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.CityUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ForecastDomainToUiMapper
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiState
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiEvent
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.usecase.ObserveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    observeSettings: ObserveSettingsUseCase,
    private val getCurrentLocationForecast: GetCurrentLocationForecastUseCase,
    private val getForecastByCity: GetForecastByCityUseCase,
    private val searchCities: SearchCitiesUseCase,
    private val forecastUiMapper: ForecastDomainToUiMapper,
    private val cityUiMapper: CityUiMapper,
    private val errorUiMapper: ErrorUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(CitySearchUiState())
    val searchState: StateFlow<CitySearchUiState> = _searchState.asStateFlow()

    private val _events = Channel<ForecastUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var settings: AppSettings = AppSettings.DEFAULT
    private var settingsInitialized = false
    private var lastForecast: Forecast? = null
    private var lastSelectedCity: City? = null
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            observeSettings().collect { newSettings ->
                val unitsChanged = settingsInitialized && newSettings.affectsForecastUnits(settings)
                val firstLoad = !settingsInitialized
                settings = newSettings
                settingsInitialized = true
                when {
                    firstLoad -> reload()
                    unitsChanged -> reload()
                }
            }
        }
    }

    fun onAction(action: ForecastUiAction) {
        when (action) {
            ForecastUiAction.OnRetryClick,
            ForecastUiAction.OnRefresh,
            -> reload()
            ForecastUiAction.OnUseCurrentLocationClick -> {
                lastSelectedCity = null
                reload()
            }
            is ForecastUiAction.OnPermissionResult ->
                if (action.granted) {
                    lastSelectedCity = null
                    reload()
                } else {
                    _uiState.value = ForecastUiState.PermissionRequired()
                }
            is ForecastUiAction.OnCitySelected -> selectCity(action.city)
        }
    }

    fun onSearchAction(action: CitySearchUiAction) {
        when (action) {
            is CitySearchUiAction.OnQueryChanged -> onQueryChanged(action.query)
            CitySearchUiAction.OnClearQuery -> onQueryChanged("")
            is CitySearchUiAction.OnCitySelected -> {
                addToRecent(action.city)
                selectCity(action.city)
            }
            CitySearchUiAction.OnUseCurrentLocation -> {
                lastSelectedCity = null
                reload()
                emitEvent(ForecastUiEvent.NavigateToHome)
            }
        }
    }

    private fun selectCity(cityUi: CityUiModel) {
        val city = cityUiMapper.toDomain(cityUi)
        lastSelectedCity = city
        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            handleForecastResult(getForecastByCity(city, settings))
            emitEvent(ForecastUiEvent.NavigateToHome)
        }
    }

    private fun reload() {
        val city = lastSelectedCity
        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            val result = if (city == null) {
                getCurrentLocationForecast(settings)
            } else {
                getForecastByCity(city, settings)
            }
            handleForecastResult(result)
        }
    }

    private fun handleForecastResult(result: AppResult<Forecast>) {
        when (result) {
            is AppResult.Success -> {
                lastForecast = result.value
                _uiState.value = ForecastUiState.Content(
                    forecast = forecastUiMapper.map(result.value, settings.measurementSystem),
                )
            }
            is AppResult.Failure -> _uiState.value = when (result.error) {
                AppError.NoLocationPermission -> ForecastUiState.PermissionRequired()
                else -> ForecastUiState.Error(errorUiMapper.map(result.error))
            }
        }
    }

    private fun onQueryChanged(query: String) {
        _searchState.update { it.copy(query = query, isSearching = query.isNotBlank()) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchState.update { it.copy(results = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            when (val result = searchCities(query)) {
                is AppResult.Success -> _searchState.update {
                    it.copy(results = result.value.map(cityUiMapper::toUi), isSearching = false)
                }
                is AppResult.Failure -> {
                    _searchState.update { it.copy(results = emptyList(), isSearching = false) }
                    emitEvent(ForecastUiEvent.ShowMessage(errorUiMapper.map(result.error).message))
                }
            }
        }
    }

    private fun addToRecent(city: CityUiModel) {
        _searchState.update { state ->
            val updated = (listOf(city) + state.recent)
                .distinctBy { it.name to it.region }
                .take(MAX_RECENT)
            state.copy(recent = updated)
        }
    }

    private fun emitEvent(event: ForecastUiEvent) {
        viewModelScope.launch { _events.send(event) }
    }

    private fun AppSettings.affectsForecastUnits(other: AppSettings): Boolean =
        temperatureUnit != other.temperatureUnit || measurementSystem != other.measurementSystem

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
        const val MAX_RECENT = 5
    }
}