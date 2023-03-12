package co.develhope.meteoapp.network.interfaces

import co.develhope.meteoapp.model.HomeScreenItem
import org.threeten.bp.OffsetDateTime

interface OnItemClickListener {
    fun viewDailyScreen(
        forecastDetails: HomeScreenItem.Forecast, position: OffsetDateTime)
}
