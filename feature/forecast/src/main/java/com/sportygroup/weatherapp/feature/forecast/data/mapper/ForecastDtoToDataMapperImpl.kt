package com.sportygroup.weatherapp.feature.forecast.data.mapper

import com.sportygroup.weatherapp.feature.forecast.data.model.CurrentDataModel
import com.sportygroup.weatherapp.feature.forecast.data.model.DailyEntryDataModel
import com.sportygroup.weatherapp.feature.forecast.data.model.ForecastDataModel
import com.sportygroup.weatherapp.feature.forecast.data.model.HourlyEntryDataModel
import com.sportygroup.weatherapp.feature.forecast.data.remote.dto.ForecastResponseDto
import javax.inject.Inject

/** Zips Open-Meteo's parallel arrays into row objects and coalesces nullable fields. */
class ForecastDtoToDataMapperImpl @Inject constructor() : ForecastDtoToDataMapper {

    override fun map(dto: ForecastResponseDto): ForecastDataModel {
        val current = requireNotNull(dto.current) {
            "dto.current must not be null; callers must validate before invoking map()"
        }
        return ForecastDataModel(
            latitude = dto.latitude,
            longitude = dto.longitude,
            timezone = dto.timezone,
            current = CurrentDataModel(
                time = current.time,
                temperature = current.temperature ?: 0.0,
                apparentTemperature = current.apparentTemperature ?: current.temperature ?: 0.0,
                humidityPercent = current.relativeHumidity ?: 0,
                weatherCode = current.weatherCode ?: -1,
                windSpeed = current.windSpeed ?: 0.0,
                pressureHpa = current.pressure ?: 0.0,
            ),
            hourly = mapHourly(dto),
            daily = mapDaily(dto),
        )
    }

    private fun mapHourly(dto: ForecastResponseDto): List<HourlyEntryDataModel> {
        val h = dto.hourly ?: return emptyList()
        return h.time.indices.map { i ->
            HourlyEntryDataModel(
                time = h.time[i],
                temperature = h.temperature.getOrNull(i) ?: 0.0,
                weatherCode = h.weatherCode.getOrNull(i) ?: -1,
                precipitationProbability = h.precipitationProbability.getOrNull(i) ?: 0,
            )
        }
    }

    private fun mapDaily(dto: ForecastResponseDto): List<DailyEntryDataModel> {
        val d = dto.daily ?: return emptyList()
        return d.time.indices.map { i ->
            DailyEntryDataModel(
                date = d.time[i],
                weatherCode = d.weatherCode.getOrNull(i) ?: -1,
                high = d.temperatureMax.getOrNull(i) ?: 0.0,
                low = d.temperatureMin.getOrNull(i) ?: 0.0,
                precipitationProbability = d.precipitationProbabilityMax.getOrNull(i) ?: 0,
            )
        }
    }
}
