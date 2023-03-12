package co.develhope.meteoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.meteoapp.states.HomeScrApiState
import co.develhope.meteoapp.network.ForecastNetworkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScrViewModel : ViewModel() {

    private var weeklyApiState = MutableLiveData<HomeScrApiState>()
    val weeklyApiStateResult: LiveData<HomeScrApiState>
        get() = weeklyApiState

    fun retrieveData() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                weeklyApiState.value = HomeScrApiState.Loading
                weeklyApiState.value = HomeScrApiState.Success(ForecastNetworkModel.getForecastSummary())
            }catch (e:Exception){
                e.printStackTrace()
                weeklyApiState.value = HomeScrApiState.Empty
                weeklyApiState.value = HomeScrApiState.Failure(e)
                Log.e("Connect Failed", e.toString())
            }
        }
    }
}