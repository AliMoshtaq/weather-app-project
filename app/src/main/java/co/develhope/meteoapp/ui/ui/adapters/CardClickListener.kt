package co.develhope.meteoapp.ui.ui.adapters

import co.develhope.meteoapp.data.dataModel.ForecastScreenItem
import org.threeten.bp.OffsetDateTime

interface CardClickListener {
    fun viewDailyScreen(
        forecastDetails: ForecastScreenItem.Forecast, position: OffsetDateTime)
}
