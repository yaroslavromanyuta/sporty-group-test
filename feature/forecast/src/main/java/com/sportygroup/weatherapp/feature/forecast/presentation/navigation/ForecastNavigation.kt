package com.sportygroup.weatherapp.feature.forecast.presentation.navigation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiEvent
import kotlinx.coroutines.flow.collectLatest

/**
 * Registers the forecast feature graph. The feature owns its routes; the host app simply
 * calls this from its root [androidx.navigation.compose.NavHost].
 */
fun NavGraphBuilder.forecastGraph(
    navController: NavController,
    onOpenSettings: () -> Unit,
) {
    navigation(
        startDestination = ForecastDestinations.HOME,
        route = ForecastDestinations.GRAPH,
    ) {
        composable(ForecastDestinations.HOME) { entry ->
            val viewModel = entry.sharedForecastViewModel(navController)
            ForecastRoute(
                viewModel = viewModel,
                onOpenSearch = { navController.navigate(ForecastDestinations.SEARCH) },
                onOpenSettings = onOpenSettings,
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
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Permission handling lives in the UI layer; the ViewModel never touches Android APIs.
    val requestLocation = rememberLocationPermissionRequester(
        onGranted = viewModel::onLocationPermissionGranted,
        onDenied = viewModel::onLocationPermissionDenied,
        onRequesting = viewModel::onLocationPermissionRequested,
    )

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            if (event is ForecastUiEvent.ShowMessage) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ForecastScreen(
        state = state,
        onAction = viewModel::onAction,
        onUseCurrentLocation = requestLocation,
        onOpenSearch = onOpenSearch,
        onOpenSettings = onOpenSettings,
    )
}

@Composable
private fun CitySearchRoute(
    viewModel: ForecastViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.searchState.collectAsStateWithLifecycle()

    val requestLocation = rememberLocationPermissionRequester(
        onGranted = {
            viewModel.onLocationPermissionGranted()
            onBack()
        },
        onDenied = {
            viewModel.onLocationPermissionDenied()
            onBack()
        },
        onRequesting = viewModel::onLocationPermissionRequested,
    )

    CitySearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CitySearchUiAction.OnCitySelected -> {
                    viewModel.onSearchAction(action)
                    onBack()
                }
                CitySearchUiAction.OnUseCurrentLocation -> requestLocation()
                else -> viewModel.onSearchAction(action)
            }
        },
        onBack = onBack,
    )
}

/**
 * Builds a callback that requests location permission only when invoked (from a button
 * click). If the permission is already granted it short-circuits to [onGranted]; otherwise
 * it launches the system dialog via [ActivityResultContracts.RequestPermission] and reports
 * the outcome. The launch never happens during composition.
 */
@Composable
private fun rememberLocationPermissionRequester(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onRequesting: () -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ ->
        // Re-check the real permission state (handles "approximate only" grants).
        if (context.hasLocationPermission()) onGranted() else onDenied()
    }
    // A fresh lambda is fine: it is only invoked from a click, never during composition.
    return {
        if (context.hasLocationPermission()) {
            onGranted()
        } else {
            onRequesting()
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

private fun Context.hasLocationPermission(): Boolean {
    val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
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