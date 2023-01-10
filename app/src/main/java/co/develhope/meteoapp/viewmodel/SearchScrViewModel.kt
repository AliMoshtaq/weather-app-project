package co.develhope.meteoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.develhope.meteoapp.data.dataModel.CityUpdate
import co.develhope.meteoapp.data.dataModel.LocationData
import co.develhope.meteoapp.data.dataModel.Cities
import co.develhope.meteoapp.data.dataTransferObject.DailySummary
import co.develhope.meteoapp.data.repository.local.CityRepository
import co.develhope.meteoapp.data.repository.local.LocationProvider
import co.develhope.meteoapp.data.repository.remote.WeatherRepository
import co.develhope.meteoapp.utils.RequestCompleteListener
import co.develhope.meteoapp.utils.Resource
import co.develhope.meteoapp.utils.error
import co.develhope.meteoapp.utils.info
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchScrViewModel: ViewModel() {

    private val tag = "ViewModel"

    //location live data
    val locationLiveData = MutableLiveData<LocationData>()
    val locationLiveDataFailure = MutableLiveData<String>()

    //weatherByLocation live data
    private val weatherByLocation = MutableLiveData<Resource<DailySummary>>()

    //cityBySearch live data
    val cityByQuery = MutableLiveData<Resource<List<Cities>>>()

    //weatherByCityID live data
    private val weatherByCityID = MutableLiveData<Resource<DailySummary>>()


    fun getCurrentLocation(model: LocationProvider){
        model.getUserCurrentLocation(object : RequestCompleteListener<LocationData> {
            override fun onRequestCompleted(data: LocationData) {
                locationLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                locationLiveDataFailure.postValue(errorMessage)
            }
        })
    }

    /**
     * Weather by Location call
     */
    fun getWeatherByLocation(model: WeatherRepository,lat:String,lon:String) {
        viewModelScope.launch {  safeWeatherByLocationFetch(model,lat,lon) }
    }

    private suspend fun safeWeatherByLocationFetch(model: WeatherRepository,lat: String,lon: String) {
        weatherByLocation.postValue(Resource.loading(null))
        try {
            val response = model.getWeatherByLocation(lat,lon)
            weatherByLocation.postValue(handleWeatherResponse(response))
        } catch (t:Throwable){
            when(t){
                is IOException -> weatherByLocation.postValue(Resource.error(null,"Network Failure"))
                else -> weatherByLocation.postValue(Resource.error(null,t.localizedMessage))
            }
        }
    }

    /**
     * Weather by CityID call
     */
    fun getWeatherByCityID(model: WeatherRepository,id:String){
        viewModelScope.launch { safeWeatherByCityIDFetch(model,id) }
    }

    private suspend fun safeWeatherByCityIDFetch(model: WeatherRepository, id: String) {
        weatherByCityID.postValue(Resource.loading(null))
        try {
            val response = model.getWeatherByCityID(id)
            weatherByCityID.postValue(handleWeatherResponse(response))
        } catch (t:Throwable) {
            when(t){
                is IOException -> weatherByCityID.postValue(Resource.error(null,"Network Failure"))
                else -> weatherByCityID.postValue(Resource.error(null,t.localizedMessage))
            }
        }
    }

    private fun handleWeatherResponse(response: Response<DailySummary>): Resource<DailySummary> {
        return if (response.isSuccessful) Resource.success(response.body()) else Resource.error(null,"Error: ${response.errorBody()}")
    }

    /**
     * City by query call
     */
    fun getCityByQuery(model: CityRepository, query:String) = viewModelScope.launch { safeCityByQueryFetch(model,query) }

    private suspend fun safeCityByQueryFetch(model: CityRepository, query: String) {
        cityByQuery.postValue(Resource.loading(null))
        try {
            val response = model.searchCities(key = query)
            cityByQuery.postValue(handleCitySearch(response))
        } catch (t:Throwable){
            when(t) {
                is IOException -> cityByQuery.postValue(Resource.error(null,"Network Failure"))
                else -> {
                    cityByQuery.postValue(Resource.error(null,t.localizedMessage))
                    error(tag, t.localizedMessage!!)
                }
            }
        }
    }

    private fun handleCitySearch(response: List<Cities>): Resource<List<Cities>> = Resource.success(response)

    /**
     * Update City call
     */
    fun updateSavedCities(model: CityRepository,obj: CityUpdate) = viewModelScope.launch {
        try {
            val info = model.updateSavedCities(obj)
            info(tag,"Success: Updating City DB: $info")
        } catch (e:Exception) {
            e.stackTrace
            error(tag,"Error: Updating City DB: ${e.localizedMessage}")}
    }

    /**
     * Saved City call
     */
    fun getSavedCities(model: CityRepository,key:Int) =  model.getSavedCities(key)

    /**
     * Delete City call
     */
    fun deleteSavedCities(model: CityRepository,cities: Cities)= viewModelScope.launch {
        try {
            model.deleteSavedCities(cities)
        } catch (e:Exception) {
            e.stackTrace
            error(tag,"Error: Deleting City DB: ${e.localizedMessage}")
        }
    }
}