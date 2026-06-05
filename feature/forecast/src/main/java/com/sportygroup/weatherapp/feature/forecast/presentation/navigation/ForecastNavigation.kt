package com.sportygroup.weatherapp.feature.forecast.presentation.navigation

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sportygroup.weatherapp.feature.forecast.presentation.CitySearchScreen
import com.sportygroup.weatherapp.feature.forecast.presentation.ForecastScreen
import com.sportygroup.weatherapp.feature.forecast.presentation.ForecastViewModel
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiEvent
import kotlinx.coroutines.flow.collectLatest

/**
 * Registers the forecast feature graph. The feature owns its routes; the host app simply
 * calls this from its root [androidx.navigation.compose.NavHost].
 */
fun NavGraphBuilder.forecastGraph(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    navigation(
        startDestination = ForecastDestinations.HOME,
        route = ForecastDestinations.GRAPH,
    ) {
        composable(ForecastDestinations.HOME) { entry ->
            val viewModel = entry.sharedForecastViewModel(navController)
            ForecastRoute(
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onOpenSearch = { navController.navigate(ForecastDestinations.SEARCH) },
            )
        }
        composable(ForecastDestinations.SEARCH) { entry ->
            val viewModel = entry.sharedForecastViewModel(navController)
            CitySearchRoute(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun ForecastRoute(
    viewModel: ForecastViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onOpenSearch: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted = result.values.any { it }
        viewModel.onAction(ForecastUiAction.OnPermissionResult(granted))
    }

    androidx.compose.runtime.LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            if (event is ForecastUiEvent.ShowMessage) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ForecastScreen(
        state = state,
        isDarkTheme = isDarkTheme,
        onAction = { action ->
            if (action is ForecastUiAction.OnUseCurrentLocationClick) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            } else {
                viewModel.onAction(action)
            }
        },
        onToggleTheme = onToggleTheme,
        onOpenSearch = onOpenSearch,
    )
}

@Composable
private fun CitySearchRoute(
    viewModel: ForecastViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.searchState.collectAsStateWithLifecycle()

    CitySearchScreen(
        state = state,
        onAction = { action ->
            viewModel.onSearchAction(action)
            if (action is CitySearchUiAction.OnCitySelected ||
                action is CitySearchUiAction.OnUseCurrentLocation
            ) {
                onBack()
            }
        },
        onBack = onBack,
    )
}

@Composable
private fun androidx.navigation.NavBackStackEntry.sharedForecastViewModel(
    navController: NavController,
): ForecastViewModel {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(ForecastDestinations.GRAPH)
    }
    return hiltViewModel(parentEntry)
}