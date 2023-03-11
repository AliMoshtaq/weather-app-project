package co.develhope.meteoapp.model

sealed class HomeScrApiState {
    object Loading                              : HomeScrApiState()
    class Failure(val e: Throwable)             : HomeScrApiState()
    class Success(val data: List<WeeklyCard>)   : HomeScrApiState()
    object Empty                                : HomeScrApiState()
}
