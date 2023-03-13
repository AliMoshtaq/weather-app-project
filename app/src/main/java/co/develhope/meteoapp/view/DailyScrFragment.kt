package co.develhope.meteoapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.develhope.meteoapp.databinding.FragmentDailyBinding
import co.develhope.meteoapp.model.DailyForecast
import co.develhope.meteoapp.model.DailyScreenItems
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.model.WeatherDescription
import co.develhope.meteoapp.states.DailyApiState
import co.develhope.meteoapp.view.adapter.DailyScrAdapter
import co.develhope.meteoapp.viewmodel.DailyScrViewModel
import org.threeten.bp.OffsetDateTime

class DailyScrFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private val viewModel: DailyScrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using the FragmentDailyBinding class
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe the data and retrieve it
        observeData()
        viewModel.retrieveData()
        // Setup the refresh listener
        setupRefreshListener()
    }

    private fun setupRefreshListener() {
        binding.swipeRefreshDailyScreen.setOnRefreshListener {
            // Retrieve the data again when the screen is refreshed
            viewModel.retrieveData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the back press
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Navigate back to the MainActivity when the back button is pressed
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    (activity as MainActivity).startActivity(intent)
                    (activity as MainActivity).overridePendingTransition(
                        com.google.android.material.R.anim.abc_popup_enter,
                        com.google.android.material.R.anim.abc_popup_exit
                    )
                }
            }
        )
    }

    private fun setupDailyForecastRV(hourlyForecastList: MutableList<DailyForecast>) {
        // Sort the hourly forecast list by hour
        hourlyForecastList.sortedBy { it.date.hour }
        // Set up the DailyScrAdapter
        val dailyScrAdapter =
            if (hourlyForecastList[0].date.dayOfYear == OffsetDateTime.now().dayOfYear) {
                // If it's the current day, show the forecast for the current time
                getHourlyForecasts(hourlyForecastList)
                val itemToShow: List<DailyScreenItems> =
                    generateDailyScreenUI(getHourlyForecasts(hourlyForecastList))
                DailyScrAdapter(itemToShow)
            } else {
                // Otherwise, show the entire daily forecast
                val itemToShow: List<DailyScreenItems> = generateDailyScreenUI(hourlyForecastList)
                DailyScrAdapter(itemToShow)
            }
        // Set up the RecyclerView
        binding.HourlyRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = dailyScrAdapter
        }
    }

    private fun getHourlyForecasts(hourlyForecastList: MutableList<DailyForecast>): MutableList<DailyForecast> {
        val list: MutableList<DailyForecast> = mutableListOf()
        if (OffsetDateTime.now().minute <= 29) {
            // If it's the first half of the hour, show the forecast for the current hour and later hours
            list.addAll(hourlyForecastList.filter {
                it.date.hour >= OffsetDateTime.now().hour
            })
        } else {
            // If it's the second half of the hour, show the forecast for later hours only
            list.addAll(hourlyForecastList.filter {
                it.date.hour > OffsetDateTime.now().hour
            })
        }
        return list
    }


    private fun generateDailyScreenUI(dailyForecastList: MutableList<DailyForecast>): List<DailyScreenItems> {
        val dailyScreenItemsList = arrayListOf<DailyScreenItems>()
        // Add the title item to the list
        dailyScreenItemsList.add(
            DailyScreenItems.Title(
                ForecastModel.getDailyForecastData()?.date ?: OffsetDateTime.now(),
                "Marino",
                "Roma",
                WeatherDescription.PARTLY_CLOUDY
            )
        )
        // Add the hourly forecast items to the list
        dailyScreenItemsList.addAll(
            dailyForecastList.map { DailyScreenItems.HourlyForecast(it) }
        )
        return dailyScreenItemsList
    }

    private fun observeData() {
        viewModel.dailyDataListResult.observe(viewLifecycleOwner) {
            // Observe the state of the daily data list result and handle it accordingly
            when (it) {
                is DailyApiState.Failure -> {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    if (binding.swipeRefreshDailyScreen.isRefreshing) {
                        binding.swipeRefreshDailyScreen.isRefreshing = false
                    }
                    // Show an error dialog and retrieve the data again when the user taps Retry
                    ErrorDialogFragment.show(
                        childFragmentManager,
                    ) { viewModel.retrieveData() }
                }
                is DailyApiState.Loading -> Unit
                is DailyApiState.Success -> {
                    // Set up the RecyclerView with the retrieved data
                    setupDailyForecastRV(it.data)
                    if (binding.swipeRefreshDailyScreen.isRefreshing) {
                        binding.swipeRefreshDailyScreen.isRefreshing = false
                    }
                }
            }
        }
    }
}
