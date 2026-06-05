package com.sportygroup.weatherapp.feature.forecast.domain.usecase

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.location.CurrentCityNameResolver
import com.sportygroup.weatherapp.core.location.CurrentLocationProvider
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import javax.inject.Inject

/**
 * Full "current location" flow: verify permission, resolve coordinates, fetch the
 * forecast and try to attach a friendly city name. Falls back to "Current location".
 */
class GetCurrentLocationForecastUseCase @Inject constructor(
    private val locationProvider: CurrentLocationProvider,
    private val cityNameResolver: CurrentCityNameResolver,
    private val repository: ForecastRepository,
) {
    suspend operator fun invoke(): AppResult<Forecast> {
        if (!locationProvider.hasLocationPermission()) {
            return AppResult.Failure(AppError.NoLocationPermission)
        }
        return when (val coordinates = locationProvider.getCurrentCoordinates()) {
            is AppResult.Failure -> coordinates
            is AppResult.Success -> {
                when (val forecast = repository.getForecastByCoordinates(coordinates.value)) {
                    is AppResult.Failure -> forecast
                    is AppResult.Success -> {
                        val resolved = cityNameResolver.resolve(coordinates.value)
                        val city = (resolved ?: forecast.value.city).copy(isCurrentLocation = true)
                        AppResult.Success(forecast.value.copy(city = city))
                    }
                }
            }
        }
    }
}