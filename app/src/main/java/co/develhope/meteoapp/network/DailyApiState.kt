package co.develhope.meteoapp.network

import co.develhope.meteoapp.data.dataModel.DailyForecast

sealed class DailyApiState {
    object Loading                                      : DailyApiState()
    class Failure(val e: Throwable)                     : DailyApiState()
    class Success(val data: MutableList<DailyForecast>) : DailyApiState()
}