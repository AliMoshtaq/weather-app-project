package co.develhope.meteoapp.model.dto

import co.develhope.meteoapp.model.CurrentWeather
import co.develhope.meteoapp.model.WeatherDescription
import co.develhope.meteoapp.model.WeeklyForecast
import co.develhope.meteoapp.model.getWeatherDescription
import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

data class WeeklyWeatherDTO(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    @SerializedName("daily")
    val dailyDTO: DailyDTO,
    @SerializedName("daily_units")
    val dailyUnitsDTO: DailyUnits,
    @SerializedName("elevation")
    val elevation: Double,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int
) {
    data class DailyDTO(
        @SerializedName("precipitation_sum")
        val precipitationSum: List<Double>,
        @SerializedName("rain_sum")
        val rainSum: List<Double>,
        @SerializedName("temperature_2m_max")
        val temperature2mMax: List<Double>,
        @SerializedName("temperature_2m_min")
        val temperature2mMin: List<Double>,
        @SerializedName("time")
        val time: List<OffsetDateTime>,
        @SerializedName("weathercode")
        val weathercode: List<Int>,
        @SerializedName("windspeed_10m_max")
        val windspeed10mmax: List<Double>
    ) {
        fun mapToDomain(): List<WeeklyForecast> {
            return this.time.mapIndexed { index, date ->
                WeeklyForecast(
                    date          = date,
                    minTemp       = this.temperature2mMin.getOrNull(index)?.toInt() ?: 0,
                    maxTemp       = this.temperature2mMax.getOrNull(index)?.toInt() ?: 0,
                    precipitation = this.precipitationSum.getOrNull(index)?.toInt() ?: 0,
                    wind          = this.windspeed10mmax.getOrNull(index)?.toInt() ?: 0,
                    weather       = this.weathercode.getOrNull(index)?.getWeatherDescription() ?: WeatherDescription.CLEAR_SKY
                )
            }
        }
    }
    data class DailyUnits(
        @SerializedName("precipitation_sum")
        val precipitationSum: String,
        @SerializedName("rain_sum")
        val rainSum: String,
        @SerializedName("sunrise")
        val sunrise: String,
        @SerializedName("sunset")
        val sunset: String,
        @SerializedName("temperature_2m_max")
        val temperature2mMax: String,
        @SerializedName("temperature_2m_min")
        val temperature2mMin: String,
        @SerializedName("time")
        val time: String,
        @SerializedName("weathercode")
        val weathercode: String
    )
}