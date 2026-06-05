package com.sportygroup.weatherapp.feature.forecast.data

import com.sportygroup.weatherapp.core.common.AppError
import com.sportygroup.weatherapp.core.common.AppResult
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultCityDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultCityDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultForecastDataToDomainMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.DefaultForecastDtoToDataMapper
import com.sportygroup.weatherapp.feature.forecast.data.mapper.ForecastUnitsMapper
import com.sportygroup.weatherapp.feature.forecast.data.remote.DefaultCitySearchRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.DefaultForecastRemoteDataSource
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.ForecastApi
import com.sportygroup.weatherapp.feature.forecast.data.remote.api.GeocodingApi
import com.sportygroup.weatherapp.feature.forecast.data.repository.ForecastRepositoryImpl
import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.testutil.TestDispatcherProvider
import com.sportygroup.weatherapp.lib.settings.model.AppSettings
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ForecastRepositoryIntegrationTest {

    private lateinit var server: MockWebServer
    private lateinit var repository: ForecastRepositoryImpl

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        repository = ForecastRepositoryImpl(
            forecastRemote = DefaultForecastRemoteDataSource(retrofit.create(ForecastApi::class.java)),
            citySearchRemote = DefaultCitySearchRemoteDataSource(retrofit.create(GeocodingApi::class.java)),
            forecastDtoToData = DefaultForecastDtoToDataMapper(),
            forecastDataToDomain = DefaultForecastDataToDomainMapper(),
            cityDtoToData = DefaultCityDtoToDataMapper(),
            cityDataToDomain = DefaultCityDataToDomainMapper(),
            forecastUnitsMapper = ForecastUnitsMapper(),
            dispatchers = TestDispatcherProvider(),
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses a successful forecast response`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))

        val result = repository.getForecast(
            City("Malaga", "Spain", Coordinates(36.7, -4.4)),
            AppSettings.DEFAULT,
        )

        assertTrue(result is AppResult.Success)
        val forecast = (result as AppResult.Success).value
        assertEquals(24.0, forecast.current.temperature, 0.0)
        assertEquals(1, forecast.daily.size)
        assertEquals(2, forecast.hourly.size)
    }

    @Test
    fun `empty city search returns empty list`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val result = repository.searchCities("zzzz")

        assertTrue(result is AppResult.Success)
        assertTrue((result as AppResult.Success).value.isEmpty())
    }

    @Test
    fun `forecast request sends metric unit parameters and coordinates`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))

        repository.getForecast(
            City("Malaga", "Spain", Coordinates(36.7, -4.4)),
            AppSettings(
                measurementSystem = MeasurementSystem.METRIC,
                temperatureUnit = TemperatureUnit.CELSIUS,
            ),
        )

        val url = server.takeRequest().requestUrl!!
        assertEquals("/v1/forecast", url.encodedPath)
        assertEquals("36.7", url.queryParameter("latitude"))
        assertEquals("-4.4", url.queryParameter("longitude"))
        assertEquals("celsius", url.queryParameter("temperature_unit"))
        assertEquals("kmh", url.queryParameter("wind_speed_unit"))
        assertEquals("mm", url.queryParameter("precipitation_unit"))
    }

    @Test
    fun `forecast request sends imperial unit parameters`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(FORECAST_JSON))

        repository.getForecast(
            City("Malaga", "Spain", Coordinates(36.7, -4.4)),
            AppSettings(
                measurementSystem = MeasurementSystem.IMPERIAL,
                temperatureUnit = TemperatureUnit.FAHRENHEIT,
            ),
        )

        val url = server.takeRequest().requestUrl!!
        assertEquals("fahrenheit", url.queryParameter("temperature_unit"))
        assertEquals("mph", url.queryParameter("wind_speed_unit"))
        assertEquals("inch", url.queryParameter("precipitation_unit"))
    }

    @Test
    fun `server error maps to network failure`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getForecast(
            City("Malaga", "Spain", Coordinates(36.7, -4.4)),
            AppSettings.DEFAULT,
        )

        assertEquals(AppResult.Failure(AppError.Network), result)
    }

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