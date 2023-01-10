package co.develhope.meteoapp.utils

import androidx.recyclerview.widget.DiffUtil
import co.develhope.meteoapp.data.dataModel.Cities

class DiffUtilCallback: DiffUtil.ItemCallback<Cities> (){

    override fun areItemsTheSame(oldItem: Cities, newItem: Cities): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cities, newItem: Cities): Boolean {
        return oldItem == newItem
    }
}