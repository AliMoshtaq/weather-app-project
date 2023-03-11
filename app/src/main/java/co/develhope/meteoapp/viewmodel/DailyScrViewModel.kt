package co.develhope.meteoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.model.DailyApiState
import co.develhope.meteoapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter

class DailyScrViewModel: ViewModel() {

    private var dailyDataList = MutableLiveData<DailyApiState>()
    val dailyDataListResult: LiveData<DailyApiState>
        get() = dailyDataList

    fun retrieveData(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val chosenData = ForecastModel.getDailyForecastData()
                if (chosenData != null){
                    dailyDataList.value = DailyApiState.Success(
                        RetrofitInstance.getDailyForecast(
                            chosenData.date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            chosenData.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        ).toMutableList()
                    )
                }else {
                    dailyDataList.value = DailyApiState.Failure(java.lang.Exception())
                }

            } catch (e: Exception){
                e.printStackTrace()
                dailyDataList.value = DailyApiState.Failure(e)
                Log.d("Log", e.toString())
            }
        }
    }
}