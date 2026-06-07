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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.InitialChoice())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(CitySearchUiState())
    val searchState: StateFlow<CitySearchUiState> = _searchState.asStateFlow()

    private val _events = MutableSharedFlow<ForecastUiEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<ForecastUiEvent> = _events.asSharedFlow()

    private var settings: AppSettings = AppSettings.DEFAULT
    private var settingsInitialized = false
    private var lastSelectedCity: City? = null
    /** True once the user has opted into current-location weather (after granting permission). */
    private var usingCurrentLocation = false
    private var searchJob: Job? = null

    init {
        // Observe settings for unit changes only. We intentionally do NOT load weather on
        // launch — the start screen (InitialChoice) stays the decision point until the user
        // taps "Use current location" (and grants permission) or picks a city manually.
        viewModelScope.launch {
            observeSettings().collect { newSettings ->
                val unitsChanged = settingsInitialized && newSettings.affectsForecastUnits(settings)
                settings = newSettings
                settingsInitialized = true
                if (unitsChanged && hasLoadedForecast()) {
                    reload()
                }
            }
        }
    }

    fun onAction(action: ForecastUiAction) {
        when (action) {
            ForecastUiAction.OnRetryClick,
            ForecastUiAction.OnRefresh,
            -> reload()
            is ForecastUiAction.OnCitySelected -> selectCity(action.city)
        }
    }

    /**
     * Called by the route after the user tapped "Use current location" and permission is
     * granted (already held or just granted). Android permission APIs stay in the UI layer.
     */
    fun onLocationPermissionGranted() {
        lastSelectedCity = null
        usingCurrentLocation = true
        reload()
    }

    /** Called by the route while the system permission dialog is shown. */
    fun onLocationPermissionRequested() {
        _uiState.value = ForecastUiState.RequestingPermission
    }

    /**
     * Called by the route when the user declines the permission. We stay on the start screen
     * and flag that manual search is still available. [permanently] is true when the system
     * will no longer show the permission dialog and the user must enable it from Settings.
     */
    fun onLocationPermissionDenied(permanently: Boolean = false) {
        _uiState.update { current ->
            if (current is ForecastUiState.Content) {
                // Permission revoked while forecast is visible — preserve the data so the
                // user can still see it. usingCurrentLocation stays true so the next
                // reload (e.g. pull-to-refresh) will hit NoLocationPermission and redirect
                // to the start screen with an appropriate message.
                current
            } else {
                usingCurrentLocation = false
                ForecastUiState.InitialChoice(
                    permissionDenied = true,
                    permissionPermanentlyDenied = permanently,
                )
            }
        }
    }

    /**
     * Called by the route when the screen resumes and location permission is now available
     * (e.g. the user enabled it from system Settings). Clears the denied flags so the start
     * screen offers "Use current location" again. We don't auto-load — the user still taps.
     */
    fun onLocationPermissionAvailable() {
        val current = _uiState.value
        if (current is ForecastUiState.InitialChoice &&
            (current.permissionDenied || current.permissionPermanentlyDenied)
        ) {
            _uiState.value = ForecastUiState.InitialChoice()
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
            // "Use current location" inside search is handled by the route (permission flow),
            // which then calls onLocationPermissionGranted()/Denied(); nothing to do here.
            CitySearchUiAction.OnUseCurrentLocation -> Unit
        }
    }

    private fun selectCity(cityUi: CityUiModel) {
        val city = cityUiMapper.toDomain(cityUi)
        lastSelectedCity = city
        usingCurrentLocation = false
        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            handleForecastResult(getForecastByCity(city, settings))
        }
    }

    private fun reload() {
        val city = lastSelectedCity
        if (city == null && !usingCurrentLocation) {
            // Nothing loaded yet — keep the start screen as the decision point.
            return
        }
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
                _uiState.value = ForecastUiState.Content(
                    forecast = forecastUiMapper.map(result.value, settings.measurementSystem),
                )
            }
            is AppResult.Failure -> {
                if (result.error == AppError.NoLocationPermission) {
                    usingCurrentLocation = false
                }
                _uiState.value = when (result.error) {
                    AppError.NoLocationPermission -> ForecastUiState.InitialChoice(permissionDenied = true)
                    else -> ForecastUiState.Error(errorUiMapper.map(result.error))
                }
            }
        }
    }

    private fun hasLoadedForecast(): Boolean = lastSelectedCity != null || usingCurrentLocation

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
        viewModelScope.launch { _events.emit(event) }
    }

    private fun AppSettings.affectsForecastUnits(other: AppSettings): Boolean =
        temperatureUnit != other.temperatureUnit || measurementSystem != other.measurementSystem

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
        const val MAX_RECENT = 5
    }
}