package com.sportygroup.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sportygroup.weatherapp.feature.forecast.presentation.navigation.ForecastDestinations
import com.sportygroup.weatherapp.feature.forecast.presentation.navigation.forecastGraph
import com.sportygroup.weatherapp.feature.settings.presentation.navigation.navigateToSettings
import com.sportygroup.weatherapp.feature.settings.presentation.navigation.settingsScreen

/** Root navigation host. Wires the forecast and settings feature graphs. */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ForecastDestinations.GRAPH,
    ) {
        forecastGraph(
            navController = navController,
            onOpenSettings = { navController.navigateToSettings() },
        )
        settingsScreen(
            onBack = { navController.popBackStack() }
        )
    }
}