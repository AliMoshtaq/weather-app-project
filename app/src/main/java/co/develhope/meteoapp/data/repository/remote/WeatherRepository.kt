package co.develhope.meteoapp.data.repository.remote

import co.develhope.meteoapp.network.RetrofitLocation

class WeatherRepository {

    suspend fun getWeatherByLocation(lat:String,lon:String) = RetrofitLocation.apiService.getWeatherByLocation(lat,lon)
    suspend fun getWeatherByCityID(id:String) = RetrofitLocation.apiService.getWeatherByCityID(id)
}