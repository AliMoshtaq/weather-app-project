package co.develhope.meteoapp.data.dataModel

import org.threeten.bp.OffsetDateTime

data class WeeklyCard(
    val minTemp         : Int,
    val maxTemp         : Int,
    val precipitation   : Int,
    val wind            : Int,
    val date            : OffsetDateTime,
    val weather         : Weather)

sealed class ForecastScreenItem {
    data class Forecast(val weeklyCard: WeeklyCard)         : ForecastScreenItem()
    data class Title(val city: String, val region: String)  : ForecastScreenItem()
    data class Subtitle(val subTitle: String)               : ForecastScreenItem()
}