package com.sportygroup.weatherapp.feature.forecast.presentation.navigation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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

    // Re-check on resume so granting from system Settings unblocks the start screen.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (context.hasLocationPermission()) {
            viewModel.onLocationPermissionAvailable()
        }
    }

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
        onOpenAppSettings = { context.openAppSettings() },
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
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            if (event is ForecastUiEvent.ShowMessage) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val requestLocation = rememberLocationPermissionRequester(
        onGranted = {
            viewModel.onLocationPermissionGranted()
            onBack()
        },
        onDenied = { permanently ->
            viewModel.onLocationPermissionDenied(permanently)
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
 * the outcome. [onDenied] receives `true` when the permission is permanently denied (the
 * system will no longer show the dialog), so the caller can route the user to Settings.
 * The launch never happens during composition.
 */
@Composable
private fun rememberLocationPermissionRequester(
    onGranted: () -> Unit,
    onDenied: (permanently: Boolean) -> Unit,
    onRequesting: () -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { _ ->
        // Re-check the real permission state (handles "approximate only" grants). When still
        // denied and the system won't show a rationale, the permission is permanently denied.
        when {
            context.hasLocationPermission() -> onGranted()
            else -> onDenied(context.isLocationPermissionPermanentlyDenied())
        }
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

/**
 * True when the permission is denied and the system will no longer show the request dialog
 * (i.e. the user must enable it from Settings). Evaluated right after a denial result, where
 * `shouldShowRequestPermissionRationale` returns false only for a permanent denial.
 */
private fun Context.isLocationPermissionPermanentlyDenied(): Boolean {
    val activity = findActivity() ?: return false
    return !ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
}

private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
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