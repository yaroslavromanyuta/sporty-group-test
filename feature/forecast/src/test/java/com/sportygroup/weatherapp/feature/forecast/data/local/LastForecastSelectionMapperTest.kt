package com.sportygroup.weatherapp.feature.forecast.data.local

import com.sportygroup.weatherapp.core.model.City
import com.sportygroup.weatherapp.core.model.Coordinates
import com.sportygroup.weatherapp.feature.forecast.data.local.LastForecastSelectionDataModel.Type
import com.sportygroup.weatherapp.feature.forecast.domain.model.LastForecastSelection
import org.junit.Assert.assertEquals
import org.junit.Test

class LastForecastSelectionMapperTest {

    private val mapper = LastForecastSelectionMapperImpl()

    private val lisbon = City("Lisbon", "Portugal", Coordinates(38.72, -9.14))

    @Test
    fun `current location round-trips`() {
        val data = mapper.toData(LastForecastSelection.CurrentLocation)

        assertEquals(Type.CURRENT_LOCATION, data.type)
        assertEquals(LastForecastSelection.CurrentLocation, mapper.toDomain(data))
    }

    @Test
    fun `manual city round-trips`() {
        val data = mapper.toData(LastForecastSelection.ManualCity(lisbon))

        assertEquals(Type.MANUAL_CITY, data.type)
        assertEquals(LastForecastSelection.ManualCity(lisbon), mapper.toDomain(data))
    }

    @Test
    fun `a manual entry without a city falls back to current location`() {
        val corrupt = LastForecastSelectionDataModel(type = Type.MANUAL_CITY, city = null)

        assertEquals(LastForecastSelection.CurrentLocation, mapper.toDomain(corrupt))
    }
}
