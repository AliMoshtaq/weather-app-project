package co.develhope.meteoapp.model

import org.threeten.bp.OffsetDateTime

data class WeeklyForecast(
    val minTemp         : Int,
    val maxTemp         : Int,
    val precipitation   : Int,
    val wind            : Int,
    val date            : OffsetDateTime,
    val weather         : WeatherDescription,
)

sealed class HomeScreenItem {
    data class Forecast(val weeklyForecast: WeeklyForecast) : HomeScreenItem()
    data class Title(val city: String, val region: String)  : HomeScreenItem()
    data class Subtitle(val subTitle: String)               : HomeScreenItem()
}