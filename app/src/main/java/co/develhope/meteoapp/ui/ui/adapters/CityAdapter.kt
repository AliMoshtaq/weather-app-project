package co.develhope.meteoapp.ui.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.data.dataModel.Cities
import co.develhope.meteoapp.databinding.SearchedCitiesItemBinding
import co.develhope.meteoapp.utils.DiffUtilCallback

class CityAdapter : RecyclerView.Adapter<CityAdapter.SearchBarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBarViewHolder {
        return SearchBarViewHolder(
            SearchedCitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    val diff = AsyncListDiffer(this, DiffUtilCallback())

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(searchBarViewHolder: SearchBarViewHolder, position: Int) {
        val cities = diff.currentList[position]
        searchBarViewHolder.bind(cities)

    }

    private var onParentItemClickListener: ((Cities) -> Unit)? = null
    fun setOnParentClickListener(listener: (Cities) -> Unit) {
        onParentItemClickListener = listener
    }

    private var onItemClickListener: ((Cities) -> Unit)? = null
    fun setOnItemClickListener(listener: (Cities) -> Unit) {
        onItemClickListener = listener
    }

    class SearchBarViewHolder(private val itemBinding: SearchedCitiesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        var onParentItemClickListener: ((Cities) -> Unit)? = null
        fun bind(cityItem: Cities) {
            itemBinding.tvCityName.text = itemView.context.getString(
                R.string.tv_city_name, cityItem.city
            )
            itemBinding.tvRegionName.text = itemView.context.getString(
                R.string.tv_region_name, cityItem.region
            )
            itemBinding.addButton.setOnClickListener {
                if (cityItem.isSaved == 1) {
                    itemBinding.addButton.visibility = View.GONE
                    itemBinding.tvAdded.visibility = View.VISIBLE
                } else {
                    itemBinding.tvAdded.visibility = View.GONE
                    itemBinding.addButton.visibility = View.VISIBLE
                }
                itemView.setOnClickListener {
                    onParentItemClickListener?.let { it(cityItem) }
                }
            }
        }
    }
}
