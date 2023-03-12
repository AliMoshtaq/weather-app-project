package co.develhope.meteoapp.states

import co.develhope.meteoapp.model.DailyForecast

sealed class DailyApiState {
    object Loading                                      : DailyApiState()
    class Failure(val e: Throwable)                     : DailyApiState()
    class Success(val data: MutableList<DailyForecast>) : DailyApiState()
}