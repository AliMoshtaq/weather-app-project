package co.develhope.meteoapp.model

import org.threeten.bp.OffsetDateTime

data class WeeklyCard(
    val minTemp         : Int,
    val maxTemp         : Int,
    val precipitation   : Int,
    val wind            : Int,
    val date            : OffsetDateTime,
    val weather         : WeatherDescription
)

sealed class ForecastScreenItem {
    data class Forecast(val weeklyCard: WeeklyCard)         : ForecastScreenItem()
    data class Title(val city: String, val region: String)  : ForecastScreenItem()
    data class Subtitle(val subTitle: String)               : ForecastScreenItem()
}