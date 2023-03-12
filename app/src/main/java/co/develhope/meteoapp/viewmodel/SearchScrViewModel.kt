package co.develhope.meteoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.meteoapp.model.LocationSearchEvent
import co.develhope.meteoapp.states.LocationApiState
import co.develhope.meteoapp.network.GeocodingNetworkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class SearchScrViewModel: ViewModel() {
    private val geocodingNetworkModel = GeocodingNetworkModel()

    private val _locationResult = MutableLiveData<LocationApiState>()
    val locationResult: LiveData<LocationApiState>
        get() = _locationResult

    fun send(event: LocationSearchEvent) =
        when (event) {
            is LocationSearchEvent.SearchCity -> getLocationResultData(event.city)
        }

    private fun getLocationResultData(city: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _locationResult.value = LocationApiState.Success(geocodingNetworkModel.getLocationInfo(city = city))
            } catch (e: Exception){
                _locationResult.value = LocationApiState.Error(e)
            }
        }
    }
}
