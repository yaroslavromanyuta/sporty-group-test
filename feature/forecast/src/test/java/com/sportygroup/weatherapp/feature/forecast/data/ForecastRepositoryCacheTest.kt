package com.sportygroup.weatherapp.feature.forecast.data

import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.core.model.ForecastSource
import com.sportygroup.weatherapp.feature.forecast.data.local.ForecastCacheKey
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDataToDomainMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.CityDtoToDataMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDataToDomainMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastDtoToDataMapperImpl
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastUnitsMapper
import com.sportygroup.weatherapp.feature.forecast.data.remote.CitySearchRemoteDataSourceImpl
import com.sportygroup.weatherapp.feature.forecast.data.remote.ForecastRemoteDataSourceImpl
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.ForecastApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.GeocodingApi
import com.sportygroup.weatherapp.feature.forecast.data.repository.ForecastRepositoryImpl
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeForecastLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeLastForecastSelectionLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeRecentCitiesLocalDataSource
import com.sportygroup.weatherapp.feature.forecast.testutil.TestDispatcherProvider
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ForecastRepositoryCacheTest {

    private lateinit var server: MockWebServer
    private lateinit var local: FakeForecastLocalDataSource
    private lateinit var repository: ForecastRepositoryImpl

    private val malaga = City("Malaga", "Spain", Coordinates(36.7, -4.4))

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        local = FakeForecastLocalDataSource()
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        repository = ForecastRepositoryImpl(
            forecastRemote = ForecastRemoteDataSourceImpl(retrofit.create(ForecastApi::class.java)),
            citySearchRemote = CitySearchRemoteDataSourceImpl(retrofit.create(GeocodingApi::class.java)),
            forecastLocal = local,
            recentCitiesLocal = FakeRecentCitiesLocalDataSource(),
            lastSelectionLocal = FakeLastForecastSelectionLocalDataSource(),
            forecastDtoToData = ForecastDtoToDataMapperImpl(),
            forecastDataToDomain = ForecastDataToDomainMapperImpl(),
            cityDtoToData = CityDtoToDataMapperImpl(),
            cityDataToDomain = CityDataToDomainMapperImpl(),
            forecastUnitsMapper = ForecastUnitsMapper(),
            dispatchers = TestDispatcherProvider(),
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `network success returns network-sourced forecast and saves to cache`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))

        val result = repository.getForecast(malaga, AppSettings.DEFAULT)

        assertTrue(result is AppResult.Success)
        assertEquals(ForecastSource.NETWORK, (result as AppResult.Success).value.source)
        // Saved under this location + units, so a later offline read finds it.
        assertTrue(local.read(cacheKey()) != null)
    }

    @Test
    fun `network failure falls back to cached forecast`() = runTest {
        // Seed cache via a successful call, then fail the network.
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))
        repository.getForecast(malaga, AppSettings.DEFAULT)
        server.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getForecast(malaga, AppSettings.DEFAULT)

        assertTrue(result is AppResult.Success)
        val forecast = (result as AppResult.Success).value
        assertEquals(ForecastSource.CACHE, forecast.source)
        assertFalse(forecast.isStale)
    }

    @Test
    fun `network failure with no cache returns the error`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getForecast(malaga, AppSettings.DEFAULT)

        assertTrue(result is AppResult.Failure)
    }

    @Test
    fun `stale cache is still served on network failure and flagged as stale`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))
        repository.getForecast(malaga, AppSettings.DEFAULT)
        local.staleOnRead = true
        server.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getForecast(malaga, AppSettings.DEFAULT)

        assertTrue(result is AppResult.Success)
        val forecast = (result as AppResult.Success).value
        assertEquals(ForecastSource.CACHE, forecast.source)
        assertTrue(forecast.isStale)
    }

    @Test
    fun `current-location forecast is not stored as a recent city`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))

        repository.getForecastByCoordinates(Coordinates(36.7, -4.4), AppSettings.DEFAULT)

        // The repository never records recents itself; recents come only from explicit add.
        assertTrue(repository.observeRecentCities().first().isEmpty())
    }

    private fun cacheKey() = ForecastCacheKey.from(
        malaga,
        ForecastUnitsMapper().map(AppSettings.DEFAULT),
    )

    private companion object {
        val FORECAST_JSON = """
            {
              "latitude": 36.7,
              "longitude": -4.4,
              "timezone": "Europe/Madrid",
              "current": {
                "time": "2026-06-05T12:00",
                "temperature_2m": 24.0,
                "apparent_temperature": 26.0,
                "relative_humidity_2m": 58,
                "weather_code": 2,
                "wind_speed_10m": 12.0,
                "pressure_msl": 1014.0
              },
              "hourly": {
                "time": ["2026-06-05T12:00", "2026-06-05T13:00"],
                "temperature_2m": [24.0, 25.0],
                "weather_code": [2, 0],
                "precipitation_probability": [10, 0]
              },
              "daily": {
                "time": ["2026-06-05"],
                "weather_code": [2],
                "temperature_2m_max": [26.0],
                "temperature_2m_min": [17.0],
                "precipitation_probability_max": [10]
              }
            }
        """.trimIndent()
    }
}