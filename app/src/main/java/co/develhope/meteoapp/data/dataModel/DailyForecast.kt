package co.develhope.meteoapp.data.dataModel

import org.threeten.bp.OffsetDateTime

data class DailyForecast(
    val date            : OffsetDateTime,
    val weather         : Weather,
    val temperature     : Int,
    val rainfall        : Int,
    val precip          : Int,
    val index           : Int,
    val humidity        : Int,
    val wind            : Int,
    val coverage        : Int,
    val windDirection   : WindDirection,
    val rain            : Int
)

sealed class DailyScreenItems {
    data class HourlyForecast(val dailyForecast: DailyForecast)                                                          : DailyScreenItems()
    data class Title         (val date: OffsetDateTime, val city: String, val region: String, val description: Weather)  : DailyScreenItems()
}