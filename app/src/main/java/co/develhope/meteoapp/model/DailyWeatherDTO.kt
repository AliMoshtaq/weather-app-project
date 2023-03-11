package co.develhope.meteoapp.model

import org.threeten.bp.OffsetDateTime

data class DailyWeatherDTO(

    var latitude                : Double,
    var longitude               : Double,
    var hourly                  : HourlyTransferObject,
    var generationtime_ms       : Double,
    var utc_offset_seconds      : Int,
    var timezone                : String,
    var timezone_abbreviation   : String,
    var elevation               : Int,
    var current_weather         : CurrentWeather,
    var hourly_units            : HourlyUnits
) {
    data class HourlyTransferObject(
        var time                    : List<OffsetDateTime>,
        var temperature_2m          : List<Double>,
        var rain                    : List<Double>,
        var showers                 : List<Double>,
        var snowfall                : List<Double>,
        var weathercode             : List<Int>,
        var windspeed_10m           : List<Double>,
        var relativehumidity_2m     : List<Int>,
        var apparent_temperature    : List<Double>,
        var cloudcover              : List<Int>,
        var winddirection_10m       : List<Int>
    ) {
        fun mapToDomain(): List<DailyForecast>{
            return this.time.mapIndexed { index, date ->
                DailyForecast(
                    date =          date,
                    weather =       this.weathercode.getOrNull(index)?.getWeatherDescription()?: WeatherDescription.PARTLY_CLOUDY,
                    temperature =   this.temperature_2m.getOrNull(index)?.toInt()?:0,
                    rainfall =      this.showers.getOrNull(index)?.toInt()?:0,
                    humidity =      this.relativehumidity_2m.getOrNull(index)?.toInt()?:0,
                    precip =        this.apparent_temperature.getOrNull(index)?.toInt()?:0,
                    wind =          this.windspeed_10m.getOrNull(index)?.toInt()?:0,
                    coverage =      this.cloudcover.getOrNull(index)?.toInt()?:0,
                    windDirection = this.winddirection_10m.getOrNull(index)?.getWindDirection()?: WindDirection.E,
                    rain =          this.rain.getOrNull(index)?.toInt()?:0,
                    index =         this.cloudcover.getOrNull(index)?.toInt()?:0


                )
            }
        }
    }

    data class HourlyUnits (
        var time            : String,
        var temperature_2m  : String,
        var rain            : String,
        var showers         : String,
        var snowfall        : String,
        var weathercode     : String,
        var windspeed_10m   : String
    )
}