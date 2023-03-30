package co.develhope.meteoapp.states

import co.develhope.meteoapp.model.LocationData

sealed class LocationApiState{
    data class Success (val data: List<LocationData>): LocationApiState()
    data class Error (val e: Exception)              : LocationApiState()
}