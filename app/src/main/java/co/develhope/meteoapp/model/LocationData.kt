package co.develhope.meteoapp.model

data class LocationData(
    val city: String,
    val region: String,
    val country: String,
    val population: Int,
    val latitude: Double,
    val longitude: Double
    )
sealed class LocationSearchEvent {
    data class SearchCity(val city: String) : LocationSearchEvent()
}


