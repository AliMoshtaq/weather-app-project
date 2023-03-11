package co.develhope.meteoapp.model.dto

import co.develhope.meteoapp.model.CurrentWeather
import co.develhope.meteoapp.model.WeatherDescription
import co.develhope.meteoapp.model.WeeklyCard
import co.develhope.meteoapp.model.getWeatherDescription
import org.threeten.bp.OffsetDateTime

data class WeeklyWeatherDTO(
    val current_weather         : CurrentWeather,
    val daily                   : DailyTransferObject,
    val daily_units             : DailyUnits,
    val elevation               : Double,
    val generationtime_ms       : Double,
    val latitude                : Double,
    val longitude               : Double,
    val timezone                : String,
    val timezone_abbreviation   : String,
    val utc_offset_seconds      : Int
) {
    data class DailyTransferObject(
        val precipitation_sum   : List<Double>,
        val rain_sum            : List<Double>,
        val sunrise             : List<String>,
        val sunset              : List<String>,
        val temperature_2m_max  : List<Double>,
        val temperature_2m_min  : List<Double>,
        val time                : List<OffsetDateTime>,
        val weathercode         : List<Int>,
        val windspeed_10m_max   : List<Double>
    ) {
        fun mapToDomain(): List<WeeklyCard> {
            return this.time.mapIndexed { index, date ->
                WeeklyCard(
                    date =          date,
                    minTemp =       this.temperature_2m_min.getOrNull(index)?.toInt() ?: 0,
                    maxTemp =       this.temperature_2m_max.getOrNull(index)?.toInt() ?: 0,
                    precipitation = this.precipitation_sum.getOrNull(index)?.toInt() ?: 0,
                    wind =          this.windspeed_10m_max.getOrNull(index)?.toInt() ?: 0,
                    weather =       this.weathercode.getOrNull(index)?.getWeatherDescription() ?: WeatherDescription.FOGGY
                )
            }
        }
    }
    data class DailyUnits(
        val precipitation_sum   : String,
        val rain_sum            : String,
        val sunrise             : String,
        val sunset              : String,
        val temperature_2m_max  : String,
        val temperature_2m_min  : String,
        val time                : String,
        val weathercode         : String
    )
}