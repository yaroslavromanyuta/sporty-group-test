package com.sportygroup.weatherapp.feature.forecast.data.repository

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.common.DispatcherProvider
import com.sportygroup.weatherapp.core.common.flatMap
import com.sportygroup.weatherapp.core.common.map
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastUnitsMapper
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSource
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.Forecast
import com.sportygroup.weatherapp.feature.forecast.domain.repository.ForecastRepository
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val forecastRemote: ForecastRemoteDataSource,
    private val citySearchRemote: CitySearchRemoteDataSource,
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
            forecastRemote.getForecast(city.coordinates.latitude, city.coordinates.longitude, units)
                .flatMap { dto ->
                    if (dto.current == null) return@flatMap AppResult.Failure(AppError.Network)
                    AppResult.Success(forecastDataToDomain.map(forecastDtoToData.map(dto), city))
                }
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
            forecastRemote.getForecast(coordinates.latitude, coordinates.longitude, units)
                .flatMap { dto ->
                    if (dto.current == null) return@flatMap AppResult.Failure(AppError.Network)
                    AppResult.Success(forecastDataToDomain.map(forecastDtoToData.map(dto), fallbackCity))
                }
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