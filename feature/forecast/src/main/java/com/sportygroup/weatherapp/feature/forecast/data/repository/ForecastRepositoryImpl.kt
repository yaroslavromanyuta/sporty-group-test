package com.sportygroup.weatherapp.feature.forecast.data.repository

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.common.flatMap
import com.sportygroup.weatherapp.core.common.map
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastCacheKey
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.local.LastForecastSelectionLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.local.RecentCitiesLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastUnitsMapper
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val forecastRemote: ForecastRemoteDataSource,
    private val citySearchRemote: CitySearchRemoteDataSource,
    private val forecastLocal: ForecastLocalDataSource,
    private val recentCitiesLocal: RecentCitiesLocalDataSource,
    private val lastSelectionLocal: LastForecastSelectionLocalDataSource,
    private val forecastDtoToData: ForecastDtoToDataMapper,
    private val forecastDataToDomain: ForecastDataToDomainMapper,
    private val cityDtoToData: CityDtoToDataMapper,
    private val cityDataToDomain: CityDataToDomainMapper,
    private val forecastUnitsMapper: ForecastUnitsMapper,
    private val dispatchers: DispatcherProvider,
) : ForecastRepository {

    override suspend fun getForecast(city: City, settings: AppSettings): AppResult<Forecast> =
        withContext(dispatchers.io) {
            val units = forecastUnitsMapper.map(settings)
            val cacheKey = ForecastCacheKey.from(city, units)
            forecastRemote.getForecast(city.coordinates.latitude, city.coordinates.longitude, units)
                .flatMap { dto -> mapDtoToForecast(dto, city) }
                .cacheOnSuccessOrFallback(cacheKey)
        }

    override suspend fun getForecastByCoordinates(
        coordinates: Coordinates,
        settings: AppSettings,
    ): AppResult<Forecast> =
        withContext(dispatchers.io) {
            val units = forecastUnitsMapper.map(settings)
            val fallbackCity = City(
                name = "",
                region = "",
                coordinates = coordinates,
                isCurrentLocation = true,
            )
            val cacheKey = ForecastCacheKey.from(fallbackCity, units)
            forecastRemote.getForecast(coordinates.latitude, coordinates.longitude, units)
                .flatMap { dto -> mapDtoToForecast(dto, fallbackCity) }
                .cacheOnSuccessOrFallback(cacheKey)
        }

    /**
     * Network-first caching: a successful forecast is written to the cache; a failure falls back
     * to the cached forecast for the same key (if any), otherwise the original error is returned.
     */
    private suspend fun AppResult<Forecast>.cacheOnSuccessOrFallback(
        key: ForecastCacheKey,
    ): AppResult<Forecast> = when (this) {
        is AppResult.Success -> also { forecastLocal.save(key, value) }
        is AppResult.Failure -> forecastLocal.read(key)?.let { AppResult.Success(it) } ?: this
    }

    private fun mapDtoToForecast(
        dto: ForecastResponseDto,
        city: City,
    ): AppResult<Forecast> {
        if (dto.current == null) return AppResult.Failure(AppError.Network)
        return AppResult.Success(forecastDataToDomain.map(forecastDtoToData.map(dto), city))
    }

    override fun observeRecentCities(): Flow<List<City>> = recentCitiesLocal.observe()

    override suspend fun addRecentCity(city: City) {
        withContext(dispatchers.io) { recentCitiesLocal.add(city) }
    }

    override suspend fun getLastSelection(): LastForecastSelection? =
        withContext(dispatchers.io) { lastSelectionLocal.read() }

    override suspend fun saveLastSelection(selection: LastForecastSelection) {
        withContext(dispatchers.io) { lastSelectionLocal.save(selection) }
    }

    override suspend fun searchCities(query: String): AppResult<List<City>> =
        withContext(dispatchers.io) {
            citySearchRemote.search(query).map { dto ->
                dto.results.orEmpty().map { result ->
                    cityDataToDomain.map(cityDtoToData.map(result))
                }
            }
        }
}