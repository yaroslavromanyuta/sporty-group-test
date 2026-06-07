package com.sportygroup.weatherapp.feature.forecast.presentation.state

import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel

/** State for the city search screen. */
data class CitySearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<CityUiModel> = emptyList(),
    val recent: List<CityUiModel> = emptyList(),
) {
    val hasQuery: Boolean get() = query.isNotBlank()
    val showEmpty: Boolean get() = hasQuery && !isSearching && results.isEmpty()
    val showResults: Boolean get() = hasQuery && results.isNotEmpty()
}