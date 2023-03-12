package co.develhope.meteoapp.view

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
import co.develhope.meteoapp.view.adapter.HomeScrAdapter
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.states.HomeScrApiState
import co.develhope.meteoapp.model.HomeScreenItem
import co.develhope.meteoapp.model.WeeklyForecast
import co.develhope.meteoapp.databinding.FragmentHomeBinding
import co.develhope.meteoapp.network.interfaces.OnItemClickListener
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
        toRefresh()
    }

    private fun toRefresh(){
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
        viewModel.weeklyApiStateResult.observe(viewLifecycleOwner) {
            when (it) {
                is HomeScrApiState.Failure -> {
                    ErrorDialogFragment.show(childFragmentManager) {
                        viewModel.retrieveData()
                    }
                    if (binding.swipeRefreshHomeScreen.isRefreshing) {
                        binding.swipeRefreshHomeScreen.isRefreshing = false
                    }
                }
                is HomeScrApiState.Empty -> Unit
                is HomeScrApiState.Loading -> Unit
                is HomeScrApiState.Success -> {
                    setupHomeRV(it.data)
                    if (binding.swipeRefreshHomeScreen.isRefreshing) {
                        binding.swipeRefreshHomeScreen.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun setupHomeRV(itemsList: List<WeeklyForecast>) {
        itemsList.sortedBy { it.date }
        val screenItem: List<HomeScreenItem> = generateHomeScreenItems(itemsList.toMutableList())
        val homeScrAdapter = HomeScrAdapter(screenItem, clickListener = object : OnItemClickListener {
            override fun viewDailyScreen(
                forecastDetails: HomeScreenItem.Forecast,
                position: OffsetDateTime
            ) {
                ForecastModel.forecastDetails(forecastDetails.weeklyForecast)
                navigateToDailyFragment(DailyScrFragment())
            }
        })
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = homeScrAdapter
        }
    }

    private fun generateHomeScreenItems(itemsList: MutableList<WeeklyForecast>): List<HomeScreenItem> {
        val homeList = arrayListOf<HomeScreenItem>()
        homeList.add(HomeScreenItem.Title("Marino", "Roma"))
        homeList.add(HomeScreenItem.Forecast(itemsList.first()))
        ForecastModel.todayDetails(itemsList.first())
        ForecastModel.tomorrowDetails(itemsList[1])
        homeList.add(HomeScreenItem.Subtitle("Next 5 Days"))
        itemsList.removeFirst()
        itemsList.removeLast()
        homeList.addAll(
            itemsList.map {
                HomeScreenItem.Forecast(it)
            }
        )
        return homeList
    }

    private fun navigateToDailyFragment(dailyFragment: DailyScrFragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,dailyFragment)
        fragmentTransaction.commit()
    }
}