package co.develhope.meteoapp.network

import co.develhope.meteoapp.data.dataModel.WeeklyCard

sealed class HomeScrApiState {
    object Loading                              : HomeScrApiState()
    class Failure(val e: Throwable)             : HomeScrApiState()
    class Success(val data: List<WeeklyCard>)   : HomeScrApiState()
    object Empty                                : HomeScrApiState()
}
