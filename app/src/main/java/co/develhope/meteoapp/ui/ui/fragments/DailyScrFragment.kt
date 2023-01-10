package co.develhope.meteoapp.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.develhope.meteoapp.ui.ui.adapters.DailyScrAdapter
import co.develhope.meteoapp.data.ForecastModel
import co.develhope.meteoapp.data.dataModel.DailyForecast
import co.develhope.meteoapp.data.dataModel.DailyScreenItems
import co.develhope.meteoapp.data.dataModel.Weather
import co.develhope.meteoapp.databinding.FragmentDailyBinding
import co.develhope.meteoapp.network.DailyApiState
import co.develhope.meteoapp.ui.ui.MainActivity
import co.develhope.meteoapp.viewmodel.DailyScrViewModel
import org.threeten.bp.OffsetDateTime

class DailyScrFragment : Fragment() {
    private lateinit var binding: FragmentDailyBinding
    private val viewModel: DailyScrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val CITY_ID = "city_id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        viewModel.retrieveData()
        swipeDownToRefresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    (activity as MainActivity).startActivity(intent)
                    (activity as MainActivity).overridePendingTransition(com.google.android.material.
                    R.anim.abc_popup_enter, com.google.android.material.R.anim.abc_popup_exit)
                }
            }
        )
    }

    private fun initRecyclerView(hourlyForecastList: MutableList<DailyForecast>) {

        hourlyForecastList.sortedBy { it.date.hour }
        val dailyScrAdapter = if (hourlyForecastList[0].date.dayOfYear == OffsetDateTime.now().dayOfYear){
            setHourlyItems(hourlyForecastList)
            val itemToShow: List<DailyScreenItems> = screenUI(setHourlyItems(hourlyForecastList))
            DailyScrAdapter(itemToShow)
        } else {
            val itemToShow: List<DailyScreenItems> = screenUI(hourlyForecastList)
            DailyScrAdapter(itemToShow)
        }

        binding.HourlyRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = dailyScrAdapter
        }
    }

    private fun setHourlyItems(hourlyForecastList: MutableList<DailyForecast>): MutableList<DailyForecast> {
        val list: MutableList<DailyForecast> = mutableListOf()
        if (OffsetDateTime.now().minute <= 29){
            list.addAll(hourlyForecastList.filter {
                it.date.hour >= OffsetDateTime.now().hour
            })
        } else {
            list.addAll(hourlyForecastList.filter {
                it.date.hour > OffsetDateTime.now().hour
            })
        }
        return list
    }

    private fun screenUI(dailyForecastList: MutableList<DailyForecast>):List<DailyScreenItems>{
        val dailyScreenItemsList = arrayListOf<DailyScreenItems>()
        dailyScreenItemsList.add(
            DailyScreenItems.Title(
                ForecastModel.getDailyForecastData()?.date?: OffsetDateTime.now(),
                "Marino",
                "Roma",
                Weather.PARTLY_CLOUDY
            )
        )
        dailyScreenItemsList.addAll(
            dailyForecastList.map { DailyScreenItems.HourlyForecast(it) }
        )
        return dailyScreenItemsList
    }

    private fun observeData(){
        viewModel.dailyDataListResult.observe(viewLifecycleOwner){
            when(it){
                is DailyApiState.Failure -> {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    if (binding.swipeRefreshDailyScreen.isRefreshing) {
                        binding.swipeRefreshDailyScreen.isRefreshing = false
                    }
                    ErrorScrFragment.show(
                        childFragmentManager,
                    ) {viewModel.retrieveData()}
                }
                is DailyApiState.Loading -> Unit
                is DailyApiState.Success -> {
                    initRecyclerView(it.data)
                    if (binding.swipeRefreshDailyScreen.isRefreshing) {
                        binding.swipeRefreshDailyScreen.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun swipeDownToRefresh(){
        binding.swipeRefreshDailyScreen.setOnRefreshListener {
            viewModel.retrieveData()
        }
    }
}












