package com.sportygroup.weatherapp.core.location

import android.content.Context
import android.location.Geocoder
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

/** Reverse-geocodes coordinates to a city name using the platform [Geocoder]. Best-effort. */
class AndroidCityNameResolver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: DispatcherProvider,
) : CurrentCityNameResolver {

    @Suppress("DEPRECATION")
    override suspend fun resolve(coordinates: Coordinates): City? = withContext(dispatchers.io) {
        if (!Geocoder.isPresent()) return@withContext null
        runCatching {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder
                .getFromLocation(coordinates.latitude, coordinates.longitude, 1)
                ?.firstOrNull()
                ?: return@runCatching null
            val name = address.locality ?: address.subAdminArea ?: address.adminArea
            ?: return@runCatching null
            City(
                name = name,
                region = listOfNotNull(address.adminArea, address.countryName).distinct()
                    .joinToString(", "),
                coordinates = coordinates,
                isCurrentLocation = true,
            )
        }.getOrNull()
    }
}