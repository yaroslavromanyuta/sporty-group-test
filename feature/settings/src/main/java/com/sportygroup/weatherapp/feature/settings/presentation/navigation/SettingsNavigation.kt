package com.sportygroup.weatherapp.feature.settings.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sportygroup.weatherapp.feature.settings.presentation.SettingsScreen
import com.sportygroup.weatherapp.feature.settings.presentation.SettingsViewModel

/** Route constants for the settings feature. */
object SettingsDestinations {
    const val SETTINGS = "settings"
}

/** Navigates to the settings screen. */
fun NavController.navigateToSettings() = navigate(SettingsDestinations.SETTINGS)

/** Registers the settings destination on the host [NavGraphBuilder]. */
fun NavGraphBuilder.settingsScreen(onBack: () -> Unit) {
    composable(SettingsDestinations.SETTINGS) {
        SettingsRoute(onBack = onBack)
    }
}

@Composable
private fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}