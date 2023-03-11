package co.develhope.meteoapp.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.databinding.SavedCitiesItemBinding
import co.develhope.meteoapp.model.LocationData

class SearchScreenAdapter(
    private val listLocation: List<LocationData>, private val resources: Resources, private val onItemClicked: (LocationData) -> Unit
) : RecyclerView.Adapter<SearchScreenAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = SavedCitiesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        with(listLocation[position]) {
            holder.binding.tvCityName.text = resources.getString(R.string.search_item_city, this.city, this.country)
            holder.itemView.setOnClickListener { onItemClicked(this) }
        }
    }

    override fun getItemCount(): Int = listLocation.size

    class LocationViewHolder(var binding: SavedCitiesItemBinding) : RecyclerView.ViewHolder(binding.root)

}