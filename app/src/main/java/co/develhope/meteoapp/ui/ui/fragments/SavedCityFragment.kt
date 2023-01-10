package co.develhope.meteoapp.ui.ui.fragments

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.data.dataModel.CityUpdate
import co.develhope.meteoapp.data.repository.local.CityRepository
import co.develhope.meteoapp.databinding.FragmentSavedCityBinding
import co.develhope.meteoapp.db.CityDatabase
import co.develhope.meteoapp.ui.ui.adapters.SavedCityAdapter
import co.develhope.meteoapp.utils.IOnBackPressed
import co.develhope.meteoapp.utils.RecyclerItemTouchHelper
import co.develhope.meteoapp.utils.lightStatusBar
import co.develhope.meteoapp.viewmodel.SearchScrViewModel
import com.google.android.material.snackbar.Snackbar

class SavedCityFragment : Fragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private lateinit var binding: FragmentSavedCityBinding
    private lateinit var viewModel : SearchScrViewModel
    private lateinit var repository: CityRepository
    private lateinit var mAdapter : SavedCityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[SearchScrViewModel::class.java]
        repository = CityRepository(CityDatabase(this.requireActivity()))
        mAdapter = SavedCityAdapter()

        setUpRecyclerView()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.getSavedCities(repository,1).observe(this.requireActivity()) { cities ->
            mAdapter.differ.submitList(cities)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSavedCity.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        ItemTouchHelper(RecyclerItemTouchHelper(this@SavedCityFragment)).attachToRecyclerView(binding.rvSavedCity)

        mAdapter.setOnItemClickListener {
            startActivity(Intent(this.context,DailyScrFragment::class.java).putExtra(DailyScrFragment.CITY_ID,it?.id.toString()))
        }
    }

    fun onSearchTextClicked(view: View) {
        val intent = Intent(this.context,SearchScrFragment::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(this.activity, Pair.create(binding.tvCitySearch,getString(R.string.label_search_hint)))
        startActivity(intent,options.toBundle())
        Handler(Looper.myLooper()!!).postDelayed({ activity?.finish() }, 1000)
    }

//    override fun onBackPressed(): Boolean{
//        return if ()
//    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is SavedCityAdapter.SavedCityViewHolder) {
            val pos = viewHolder.adapterPosition
            val cities = mAdapter.differ.currentList[pos]
            viewModel.updateSavedCities(CityRepository(CityDatabase(this.requireActivity())),
                CityUpdate(cities.id,0)
            )

            Snackbar.make(binding.clParent,"City removed from saved items", Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    viewModel.updateSavedCities(CityRepository(CityDatabase(this.context)),
                        CityUpdate(cities.id,1)
                    )
                }
                setBackgroundTint(resources.getColor(R.color.colorPrimary))
                setActionTextColor(resources.getColor(R.color.color_grey))
                show()
            }

        }

    }

}