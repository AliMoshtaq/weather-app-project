package co.develhope.meteoapp.model

sealed class DailyApiState {
    object Loading                                      : DailyApiState()
    class Failure(val e: Throwable)                     : DailyApiState()
    class Success(val data: MutableList<DailyForecast>) : DailyApiState()
}