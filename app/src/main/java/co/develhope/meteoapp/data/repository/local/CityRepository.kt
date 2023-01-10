package co.develhope.meteoapp.data.repository.local

import co.develhope.meteoapp.data.dataModel.CityUpdate
import co.develhope.meteoapp.data.dataModel.Cities
import co.develhope.meteoapp.db.CityDatabase

class CityRepository (private val database: CityDatabase) {

    suspend fun searchCities(key:String) = database.getCityDao().searchCity(key)
    suspend fun updateSavedCities(obj: CityUpdate) = database.getCityDao().updateSavedCity(obj)
    fun getSavedCities(key: Int) = database.getCityDao().getSavedCity(key)
    suspend fun deleteSavedCities(cities: Cities) = database.getCityDao().deleteSavedCity(cities)
}