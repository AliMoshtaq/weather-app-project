package co.develhope.meteoapp.ui.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.develhope.meteoapp.viewmodel.HomeScrViewModel
import co.develhope.meteoapp.R
import co.develhope.meteoapp.ui.ui.adapters.HomeScrAdapter
import co.develhope.meteoapp.data.ForecastModel
import co.develhope.meteoapp.network.HomeScrApiState
import co.develhope.meteoapp.data.dataModel.ForecastScreenItem
import co.develhope.meteoapp.data.dataModel.WeeklyCard
import co.develhope.meteoapp.databinding.FragmentHomeBinding
import co.develhope.meteoapp.ui.ui.adapters.CardClickListener
import org.threeten.bp.OffsetDateTime

class HomeScrFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeScrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        swipeDownToRefresh()
    }

    private fun swipeDownToRefresh(){
        binding.swipeRefreshHomeScreen.setOnRefreshListener {
            viewModel.retrieveData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val exit = QuiteAppFragment()
                    exit.show(childFragmentManager,null)
                }
            }
        )
    }

    override fun onResume() {
        viewModel.retrieveData()
        super.onResume()
    }

    private fun observeData() {
        viewModel.weeklyDataListResult.observe(viewLifecycleOwner) {
            when (it) {
                is HomeScrApiState.Failure -> {
                    ErrorScrFragment.show(childFragmentManager) {
                        viewModel.retrieveData()
                    }
                    if (binding.swipeRefreshHomeScreen.isRefreshing) {
                        binding.swipeRefreshHomeScreen.isRefreshing = false
                    }
                }
                is HomeScrApiState.Empty -> Unit
                is HomeScrApiState.Loading -> Unit
                is HomeScrApiState.Success -> {
                    initRecyclerView(it.data)
                    if (binding.swipeRefreshHomeScreen.isRefreshing) {
                        binding.swipeRefreshHomeScreen.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun initRecyclerView(itemsList: List<WeeklyCard>) {
        itemsList.sortedBy { it.date }
        val screenItem: List<ForecastScreenItem> = screenUI(itemsList.toMutableList())
        val homeScrAdapter = HomeScrAdapter(screenItem, clickListener = object : CardClickListener {
            override fun viewDailyScreen(
                forecastDetails: ForecastScreenItem.Forecast,
                position: OffsetDateTime
            ) {
                ForecastModel.forecastDetails(forecastDetails.weeklyCard)
                moveToFragment(DailyScrFragment())
            }
        })
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = homeScrAdapter
        }
    }

    private fun screenUI(itemsList: MutableList<WeeklyCard>): List<ForecastScreenItem> {
        val homeList = arrayListOf<ForecastScreenItem>()
        homeList.add(ForecastScreenItem.Title("Marino", "Roma"))
        homeList.add(ForecastScreenItem.Forecast(itemsList.first()))
        ForecastModel.todayDetails(itemsList.first())
        ForecastModel.tomorrowDetails(itemsList[1])
        homeList.add(ForecastScreenItem.Subtitle("Next 5 Days"))
        itemsList.removeFirst()
        itemsList.removeLast()
        homeList.addAll(
            itemsList.map {
                ForecastScreenItem.Forecast(it)
            }
        )
        return homeList
    }

    private fun moveToFragment(dailyFragment: DailyScrFragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,dailyFragment)
        fragmentTransaction.commit()
    }
}