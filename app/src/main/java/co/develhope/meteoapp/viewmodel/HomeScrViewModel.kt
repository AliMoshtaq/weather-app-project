package co.develhope.meteoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.meteoapp.model.HomeScrApiState
import co.develhope.meteoapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScrViewModel : ViewModel() {

    private var weeklyDataList = MutableLiveData<HomeScrApiState>()
    val weeklyDataListResult: LiveData<HomeScrApiState>
        get() = weeklyDataList

    fun retrieveData() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                weeklyDataList.value = HomeScrApiState.Loading
                weeklyDataList.value = HomeScrApiState.Success(RetrofitInstance.getForecastSummary())
                Log.d("Connect Success",":$weeklyDataListResult")
            }catch (e:Exception){
                e.printStackTrace()
                weeklyDataList.value = HomeScrApiState.Empty
                weeklyDataList.value = HomeScrApiState.Failure(e)
                Log.d("Connect Failed", e.toString())
            }
        }
    }
}