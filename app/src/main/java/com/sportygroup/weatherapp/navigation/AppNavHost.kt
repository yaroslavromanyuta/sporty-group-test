package com.sportygroup.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sportygroup.weatherapp.feature.forecast.presentation.navigation.ForecastDestinations
import com.sportygroup.weatherapp.feature.forecast.presentation.navigation.forecastGraph

/** Root navigation host. Delegates the forecast routes to the feature module. */
@Composable
fun AppNavHost(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ForecastDestinations.GRAPH,
    ) {
        forecastGraph(
            navController = navController,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
        )
    }
}