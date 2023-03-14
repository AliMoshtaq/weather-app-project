package co.develhope.meteoapp.model.dto

import co.develhope.meteoapp.model.*
import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

data class DailyWeatherDTO(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    val elevation: Double,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("hourly")
    val hourlyDTO: HourlyDTO,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int
) {
    data class HourlyDTO(
        val rain: List<Double>,
        val showers: List<Double>,
        val snowfall: List<Double>,
        @SerializedName("temperature_2m")
        val temperature2m: List<Double>,
        val time: List<OffsetDateTime>,
        val weathercode: List<Int>,
        @SerializedName("windspeed_10m")
        val windspeed10m: List<Double>,
        @SerializedName("relativehumidity_2m")
        val relativeHumidity: List<Int>,
        @SerializedName("apparent_temperature")
        val apparentTemperature: List<Double>,
        val cloudcover: List<Int>,
        @SerializedName("winddirection_10m")
        val windDirection: List<Int>
    ) {
        fun mapToDomain(): List<DailyForecast> = time.mapIndexed { index, date ->
            DailyForecast(
                date = date,
                weather = weathercode.getOrNull(index)?.getWeatherDescription() ?: WeatherDescription.PARTLY_CLOUDY,
                temperature = temperature2m.getOrNull(index)?.toInt() ?: 0,
                rainfall = showers.getOrNull(index)?.toInt() ?: 0,
                humidity = relativeHumidity.getOrNull(index)?.toInt() ?: 0,
                precip = apparentTemperature.getOrNull(index)?.toInt() ?: 0,
                wind = windspeed10m.getOrNull(index)?.toInt() ?: 0,
                coverage = cloudcover.getOrNull(index)?.toInt() ?: 0,
                windDirection = windDirection.getOrNull(index)?.getWindDirection() ?: WindDirection.N,
                rain = rain.getOrNull(index)?.toInt() ?: 0,
                index = cloudcover.getOrNull(index)?.toInt() ?: 0
            )
        }
    }

    data class HourlyUnits (
        val rain: String,
        val showers: String,
        val snowfall: String,
        val temperature2m: String,
        val time: String,
        val weathercode: String,
        val windspeed10m: String
    )
}