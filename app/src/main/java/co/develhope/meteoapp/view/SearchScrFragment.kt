package co.develhope.meteoapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.develhope.meteoapp.view.adapter.SearchScreenAdapter
import co.develhope.meteoapp.databinding.FragmentSearchBinding
import co.develhope.meteoapp.model.LocationData
import co.develhope.meteoapp.prefs
import co.develhope.meteoapp.viewmodel.LocationResult
import co.develhope.meteoapp.viewmodel.LocationSearchEvent
import co.develhope.meteoapp.viewmodel.SearchScreenViewModel
import com.google.android.material.R

class SearchScrFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        research()
        observeRepo()
    }

    private fun research() {

        binding.svSearchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.send(LocationSearchEvent.SearchCity(query.toString()))
                observeRepo()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.send(LocationSearchEvent.SearchCity(newText.toString()))
                observeRepo()
                return false
            }

        })
    }

    private fun observeRepo() {
        viewModel.locationResult.observe(viewLifecycleOwner) {
            when (it) {
                is LocationResult.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                    .show() //TODO Replace this Toast
                is LocationResult.Success -> {
                    setupUi(it.data)
                }
            }
        }
    }

    private fun setupUi(it: List<LocationData>) {
        hideSearchText(it)
        val searchScreenAdapter = SearchScreenAdapter(it, resources) {
            prefs.longitudePref = it.longitude.toFloat()
            prefs.latitudePref = it.latitude.toFloat()
            prefs.cityPref = it.city
            prefs.countryPref = it.country
            val intent = Intent(context,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            (activity as MainActivity).startActivity(intent)
            (activity as MainActivity).overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit)
        }
        Log.d("GeocodingLog", "${it.size}")
        binding.rvSearchedResult  .apply {
            layoutManager =
                LinearLayoutManager(
                    this@SearchScrFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = searchScreenAdapter
        }
    }

    private fun hideSearchText(cityList: List<LocationData>) {
        if (cityList.isEmpty()) {
            binding.tvRecentSearch.visibility = View.GONE
        } else {
            binding.tvRecentSearch.visibility = View.VISIBLE
        }
    }


}
