package com.sportygroup.weatherapp.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.Coordinates
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AndroidLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedClient: FusedLocationProviderClient,
) : CurrentLocationProvider {

    override fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentCoordinates(): AppResult<Coordinates> {
        if (!hasLocationPermission()) return AppResult.Failure(AppError.NoLocationPermission)
        return try {
            val location = fusedClient.lastLocation.await()
                ?: fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await()
            if (location != null) {
                AppResult.Success(Coordinates(location.latitude, location.longitude))
            } else {
                AppResult.Failure(AppError.LocationUnavailable)
            }
        } catch (e: SecurityException) {
            AppResult.Failure(AppError.NoLocationPermission)
        } catch (e: Exception) {
            AppResult.Failure(AppError.LocationUnavailable)
        }
    }
}