package co.develhope.meteoapp.ui.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.data.dataModel.Cities
import co.develhope.meteoapp.data.dataModel.DailyScreenItems
import co.develhope.meteoapp.data.dataModel.ForecastScreenItem
import co.develhope.meteoapp.databinding.SavedCitiesItemBinding
import co.develhope.meteoapp.utils.DiffUtilCallback


class SavedCityAdapter:
    RecyclerView.Adapter<SavedCityAdapter.SavedCityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCityViewHolder {
        return SavedCityViewHolder(
            SavedCitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(savedCityViewHolder: SavedCityViewHolder, position: Int) {
        val cities = differ.currentList[position]
        savedCityViewHolder.bind(cities)
    }


    private var onItemClickListener: ((Cities?) -> Unit)? = null
    fun setOnItemClickListener(listener: (Cities?) -> Unit) {
        onItemClickListener = listener

    }

    inner class SavedCityViewHolder(private val itemBinding: SavedCitiesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(cities: Cities?) {
            itemBinding.tvCityName.text = itemView.context.getString(
                R.string.tv_city_name, cities?.city
            )
            itemBinding.searchTempGrade.text = itemView.context.getString(
                R.string.search_item_tempGrade, cities?.temp
            )
            itemBinding.savedCitiesDescription.text = itemView.context.getString(
                R.string.search_temp_description, cities?.description
            )
            itemBinding.viewForeground
            itemBinding.viewBackground

        }
    }
}




