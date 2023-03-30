package co.develhope.meteoapp.states

import co.develhope.meteoapp.model.WeeklyForecast

sealed class HomeScrApiState {
    object Loading                                  : HomeScrApiState()
    class Failure(val e: Throwable)                 : HomeScrApiState()
    class Success(val data: List<WeeklyForecast>)   : HomeScrApiState()
    object Empty                                    : HomeScrApiState()
}
