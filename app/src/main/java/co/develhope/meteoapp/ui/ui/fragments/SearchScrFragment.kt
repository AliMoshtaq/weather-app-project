package co.develhope.meteoapp.ui.ui.fragments

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.develhope.meteoapp.R
import co.develhope.meteoapp.data.dataModel.CityUpdate
import co.develhope.meteoapp.data.dataModel.Cities
import co.develhope.meteoapp.data.repository.local.CityRepository
import co.develhope.meteoapp.databinding.FragmentSearchBinding
import co.develhope.meteoapp.db.CityDatabase
import co.develhope.meteoapp.ui.ui.MainActivity
import co.develhope.meteoapp.ui.ui.adapters.CityAdapter
import co.develhope.meteoapp.utils.Status
import co.develhope.meteoapp.viewmodel.SearchScrViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.awaitCancellation

class SearchScrFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchScrViewModel
    private lateinit var database: CityDatabase
    private lateinit var repository: CityRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SearchScrViewModel::class.java]
        database = CityDatabase(this.requireActivity())
        repository = CityRepository(database)

        binding.svSearchCity.requestFocus()

        setUpUI()
        setUpObservers()

        binding.btnCancel.setOnClickListener {
            navigateBack()
        }
    }

    private fun setUpObservers() {
        viewModel.cityByQuery.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (it.data!!.isNotEmpty()) {

                            binding.tvRecentSearch.visibility = View.VISIBLE
                            //set data to recycler view
                            setUpRecyclerView(it.data)
                        } else {
                            binding.tvRecentSearch.visibility = View.GONE
                        }
                    }
                    Status.ERROR -> {
                        showFailedView(it.message)
                    }
                    Status.LOADING -> {
                        binding.tvRecentSearch.visibility = View.GONE

                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(data: List<Cities>) {
        val cityAdapter = CityAdapter()
        binding.rvSearchedResult.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = cityAdapter
        }

        cityAdapter.diff.submitList(data)


        cityAdapter.setOnItemClickListener { viewModel.updateSavedCities(repository, CityUpdate(it.id,1)) }
        cityAdapter.setOnParentClickListener {
            startActivity(Intent(this.context,DailyScrFragment::class.java).putExtra(DailyScrFragment.CITY_ID,it.id.toString()))
        }
    }

    private fun setUpUI() {
        binding.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchedQuery = if (query!!.contains("'")) DatabaseUtils.sqlEscapeString(query).replace("'","") else query
                viewModel.getCityByQuery(repository,searchedQuery)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchedQuery = if (newText!!.contains("'")) DatabaseUtils.sqlEscapeString(newText).replace("'","") else newText
                viewModel.getCityByQuery(repository,searchedQuery)
                return false
            }

        })
    }

    private fun showFailedView(message:String?){
        binding.rvSearchedResult.visibility = View.GONE
        binding.tvNoResult.visibility = View.GONE
        binding.tvNoResult.text = message
    }
    private fun navigateBack() {
        val intent = Intent(this.context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        (activity as MainActivity).startActivity(intent)
        (activity as MainActivity).overridePendingTransition(com.google.android.material.
        R.anim.abc_popup_enter, com.google.android.material.R.anim.abc_popup_exit)
    }
}
