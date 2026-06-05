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
import com.sportygroup.weatherapp.feature.forecast.testutil.FakeStringResources
import com.sportygroup.weatherapp.feature.forecast.testutil.TestDispatcherProvider
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

/** Integration test: GeocodingApi + DTO parsing + city mappers via the repository. */
class CitySearchRepositoryIntegrationTest {

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
            stringResources = FakeStringResources(),
            dispatchers = TestDispatcherProvider(),
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `maps geocoding results to domain cities`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(GEOCODING_JSON))

        val result = repository.searchCities("Malaga")

        assertTrue(result is AppResult.Success)
        val cities = (result as AppResult.Success).value
        assertEquals(2, cities.size)
        val malaga = cities.first()
        assertEquals("Malaga", malaga.name)
        // region is built from admin1 + country
        assertEquals("Andalusia, Spain", malaga.region)
        assertEquals(36.72, malaga.coordinates.latitude, 0.0001)
        assertEquals(-4.42, malaga.coordinates.longitude, 0.0001)
    }

    @Test
    fun `search request sends query and locale parameters`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(GEOCODING_JSON))

        repository.searchCities("Malaga")

        val url = server.takeRequest().requestUrl!!
        assertEquals("/v1/search", url.encodedPath)
        assertEquals("Malaga", url.queryParameter("name"))
        assertEquals("en", url.queryParameter("language"))
        assertEquals("json", url.queryParameter("format"))
    }

    @Test
    fun `missing results field returns empty list`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val result = repository.searchCities("zzzz")

        assertTrue(result is AppResult.Success)
        assertTrue((result as AppResult.Success).value.isEmpty())
    }

    @Test
    fun `server error maps to network failure`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))

        val result = repository.searchCities("Malaga")

        assertEquals(AppResult.Failure(AppError.Network), result)
    }

    private companion object {
        val GEOCODING_JSON = """
            {
              "results": [
                {
                  "id": 1,
                  "name": "Malaga",
                  "latitude": 36.72,
                  "longitude": -4.42,
                  "country": "Spain",
                  "admin1": "Andalusia"
                },
                {
                  "id": 2,
                  "name": "Madrid",
                  "latitude": 40.42,
                  "longitude": -3.70,
                  "country": "Spain",
                  "admin1": "Madrid"
                }
              ]
            }
        """.trimIndent()
    }
}