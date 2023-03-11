package co.develhope.meteoapp.network.interfaces

import co.develhope.meteoapp.model.ForecastScreenItem
import org.threeten.bp.OffsetDateTime

interface CardClickListener {
    fun viewDailyScreen(
        forecastDetails: ForecastScreenItem.Forecast, position: OffsetDateTime)
}
