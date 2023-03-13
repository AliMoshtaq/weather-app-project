package co.develhope.meteoapp.view.adapter // Define the package name

import android.view.LayoutInflater // Import the LayoutInflater class for inflating layout XML files
import android.view.View // Import the View class for working with views
import android.view.ViewGroup // Import the ViewGroup class for working with view groups
import androidx.core.view.isVisible // Import the isVisible extension function for working with view visibility
import androidx.recyclerview.widget.RecyclerView // Import the RecyclerView class for working with a RecyclerView
import co.develhope.meteoapp.R // Import the R class for accessing resources
import co.develhope.meteoapp.databinding.DailyHourlyItemBinding // Import the binding class for the hourly forecast item
import co.develhope.meteoapp.databinding.DailyTitleItemBinding // Import the binding class for the hourly title item
import co.develhope.meteoapp.model.DailyScreenItems // Import the DailyScreenItems class for working with daily screen items
import co.develhope.meteoapp.model.ForecastModel // Import the ForecastModel class for working with forecast data
import co.develhope.meteoapp.utility.prefs // Import the prefs object for working with app preferences
import org.threeten.bp.OffsetDateTime

class DailyScrAdapter(private val newList: List<DailyScreenItems>) : // Define the adapter class and its input list
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // Extend the RecyclerView.Adapter class

    companion object { // Define a companion object
        const val HOURLY_TITLE = 0 // Define a constant for the hourly title item type
        const val HOURLY_FORECAST = 1 // Define a constant for the hourly forecast item type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder { // Override the onCreateViewHolder method
        return when (viewType) { // Use a when expression to determine the item type and return the appropriate view holder
            HOURLY_TITLE -> DailyTitleViewHolder( // If it's a title item, inflate the title item binding and return a new instance of the DailyTitleViewHolder class
                DailyTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            HOURLY_FORECAST -> DailyHourlyViewHolder( // If it's a forecast item, inflate the forecast item binding and return a new instance of the DailyHourlyViewHolder class
                DailyHourlyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type") // If the item type is invalid, throw an exception
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { // Override the onBindViewHolder method
        when (holder) { // Use a when expression to determine the item type and bind the appropriate data to the view holder
            is DailyTitleViewHolder -> holder.bind(newList[position] as DailyScreenItems.Title) // If it's a title item, cast the data to a Title object and bind it to the view holder
            is DailyHourlyViewHolder -> { // If it's a forecast item, cast the data to a HourlyForecast object and bind it to the view holder
                holder.bind(newList[position] as DailyScreenItems.HourlyForecast)
                // Hide arrow icon for all items except the first one
                if (position != 1) { // If the item position is not the first one, hide the arrow icon
                    holder.itemBinding.arrowIcon.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int { // Override the getItemViewType method to return the corresponding item type for the given position
        return when (newList[position]) { // Use a when expression to determine the data type and return the appropriate item type
            is DailyScreenItems.Title -> HOURLY_TITLE // If it's a title item, return the title item type
            is DailyScreenItems.HourlyForecast -> HOURLY_FORECAST // If it's a forecast item, return the forecast item type
        }
    }

    override fun getItemCount(): Int { // Override the getItemCount method to return the number of items in the list
        return newList.size
    }

    class DailyTitleViewHolder(private val itemBinding: DailyTitleItemBinding) : // Define the view holder class for the hourly title item
        RecyclerView.ViewHolder(itemBinding.root) { // Extend the RecyclerView.ViewHolder class
        fun bind(title: DailyScreenItems.Title) { // Define a bind method to set the view values based on the title data
            itemBinding.dayOfWeek.text = ForecastModel.setDayOfWeek(title.date.dayOfWeek.toString()) // Set the day of week text using the setDayOfWeek method from the ForecastModel class
            itemBinding.dailyDateTxt.text = itemView.context.getString( // Set the daily date text using a string resource and the setMonthOfYear method from the ForecastModel class
                R.string.daily_date,
                title.date.dayOfWeek.toString()[0] + title.date.dayOfWeek.toString().substring(1).lowercase(),
                title.date.dayOfMonth,
                ForecastModel.setMonthOfYear(title.date.month.toString())
            )
            itemBinding.dayTitle.text = itemView.context.getString( // Set the day title text using a string resource and the prefs object for accessing app preferences
                R.string.palermo_sic,
                prefs.cityPref,
                prefs.countryPref
            )
            itemBinding.dailyDescriptionTxt.text = ForecastModel.setDescription(title.description) // Set the daily description text using the setDescription method from the ForecastModel class
        }
    }

    class DailyHourlyViewHolder(val itemBinding: DailyHourlyItemBinding) : // Define the view holder class for the hourly forecast item
        RecyclerView.ViewHolder(itemBinding.root) { // Extend the RecyclerView.ViewHolder class

        fun bind(hourlyItems: DailyScreenItems.HourlyForecast) { // Define a bind method to set the view values based on the hourly forecast data
            val time = OffsetDateTime.now() // Replace with the appropriate time for the forecast item

            itemBinding.dayHour2.text = itemView.context.getString(R.string.daily_hour_second, hourlyItems.dailyForecast.date.hour) // Set the hourly text using a string resource and the hour from the hourly forecast data
            itemBinding.dailyIcon.setImageResource(ForecastModel.setIcon(hourlyItems.dailyForecast.weather,time)) // Set the image resource for the daily icon using the setIcon method from the ForecastModel class
            itemBinding.dayGrade.text = itemView.context.getString( // Set the daily temperature text using a string resource and the temperature from the hourly forecast data
                R.string.daily_temp_grade, hourlyItems.dailyForecast.temperature
            )
            itemBinding.dayHumidityGrade.text = itemView.context.getString( // Set the daily humidity text using a string resource and the rainfall from the hourly forecast data
                R.string.daily_humidity_percent, hourlyItems.dailyForecast.rainfall
            )
            itemBinding.dailyCardHumidityGrade.text = itemView.context.getString( // Set the daily card humidity text using a string resource and the humidity from the hourly forecast data
                R.string.daily_humidity_grade, hourlyItems.dailyForecast.humidity
            )
            itemBinding.dailyCardPrecipitationGrade.text = itemView.context.getString( // Set the daily card precipitation text using a string resource and the precipitation from the hourly forecast data
                R.string.daily_precipitation_grade, hourlyItems.dailyForecast.precip
            )
            itemBinding.dailyCardCoverageGrade.text = itemView.context.getString( // Set the daily card coverage text using a string resource and the coverage from the hourly forecast data
                R.string.daily_coverage_grade, hourlyItems.dailyForecast.coverage
            )
            itemBinding.dailyCardRainGrade.text = itemView.context.getString( // Set the daily card rain text using a string resource and the rain from the hourly forecast data
                R.string.daily_rain_grade, hourlyItems.dailyForecast.rain
            )
            itemBinding.dailyCardWindSpeedGrade.text = itemView.context.getString( // Set the daily card wind speed text using a string resource, the wind direction, and the wind speed from the hourly forecast data
                R.string.daily_wind_speed,
                hourlyItems.dailyForecast.windDirection,
                hourlyItems.dailyForecast.wind
            )
            itemBinding.dailyCardUvIndexGrade.text = itemView.context.getString( // Set the daily card UV index text using a string resource and the UV index from the hourly forecast data
                R.string.daily_cloudCover_grade, hourlyItems.dailyForecast.index
            )

            itemBinding.arrowIcon.setOnClickListener { // Set a click listener for the arrow icon to toggle the visibility of a card view
                val isExpanded = itemBinding.dailyCardView.visibility == View.VISIBLE // Determine if the card view is visible or not
                val rotationAngle = if (isExpanded) 0f else 180f // Determine the rotation angle based on the visibility
                itemBinding.arrowIcon.animate().rotation(rotationAngle).start() // Animate the arrow icon to rotate to the appropriate angle
                itemBinding.dailyCardView.isVisible = !isExpanded // Toggle the visibility of the card view
            }
        }
    }
}

