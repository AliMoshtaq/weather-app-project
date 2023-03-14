package co.develhope.meteoapp.view.adapter // Define the package name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.databinding.DailyHourlyItemBinding
import co.develhope.meteoapp.databinding.DailyTitleItemBinding
import co.develhope.meteoapp.model.DailyScreenItems
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.utility.prefs
import org.threeten.bp.OffsetDateTime

class DailyScrAdapter(private val newList: List<DailyScreenItems>) : // Define the adapter class and its input list
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // Extend the RecyclerView.Adapter class

    private var expandedPosition = -1
    companion object { // Define a companion object
        const val HOURLY_TITLE    = 0 // Define a constant for the hourly title item type
        const val HOURLY_FORECAST = 1 // Define a constant for the hourly forecast item type
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder { // Override the onCreateViewHolder method
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DailyTitleViewHolder -> holder.bind(newList[position] as DailyScreenItems.Title)
            is DailyHourlyViewHolder -> {
                val hourlyForecast = newList[position] as DailyScreenItems.HourlyForecast
                holder.bind(hourlyForecast)

                with(holder.binding) {
                    val currentPosition = holder.getBindingAdapterPosition()
                    arrowIcon.isVisible = currentPosition == 1
                    dailyCardView.isVisible = currentPosition == expandedPosition

                    if (currentPosition == 1) {
                        val isExpanded = currentPosition == expandedPosition
                        val rotationAngle = if (isExpanded) 180f else 0f
                        arrowIcon.rotation = rotationAngle

                        arrowIcon.setOnClickListener {
                            val newExpanded = !dailyCardView.isVisible

                            expandedPosition = if (newExpanded) {
                                val newRotationAngle = 180f
                                arrowIcon.animate().rotation(newRotationAngle).setDuration(200).start()
                                currentPosition
                            } else {
                                -1
                            }

                            dailyCardView.isVisible = newExpanded
                            notifyItemChanged(currentPosition)
                        }
                    } else {
                        arrowIcon.setOnClickListener(null)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int { // Override the getItemViewType method to return the corresponding item type for the given position
        return when (newList[position]) { // Use a when expression to determine the data type and return the appropriate item type
            is DailyScreenItems.Title          -> HOURLY_TITLE // If it's a title item, return the title item type
            is DailyScreenItems.HourlyForecast -> HOURLY_FORECAST // If it's a forecast item, return the forecast item type
        }
    }

    override fun getItemCount(): Int { // Override the getItemCount method to return the number of items in the list
        return newList.size
    }

    class DailyTitleViewHolder(private val binding: DailyTitleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: DailyScreenItems.Title) {
            with(binding) {
                // Set the day of week text using the setDayOfWeek method from the ForecastModel class
                dayOfWeek.text = ForecastModel.setDayOfWeek(title.date.dayOfWeek.toString())

                // Set the daily date text using a string resource and the setMonthOfYear method from the ForecastModel class
                dailyDateTxt.text = itemView.context.getString(
                    R.string.daily_date,
                    title.date.dayOfWeek.toString().lowercase()
                        .replaceFirstChar { it.uppercase() }, // Capitalize the first letter of the day of week string
                    title.date.dayOfMonth,
                    ForecastModel.setMonthOfYear(title.date.month.toString())
                )

                // Set the day title text using a string resource and the prefs object for accessing app preferences
                dayTitle.text = itemView.context.getString(
                    R.string.palermo_sic,
                    prefs.cityPref,
                    prefs.countryPref
                )

                // Set the daily description text using the setDescription method from the ForecastModel class
                dailyDescriptionTxt.text = ForecastModel.setDescription(title.description)
            }
        }
    }


    class DailyHourlyViewHolder(val binding: DailyHourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: DailyScreenItems.HourlyForecast) {
            with(binding) {
                val time =
                    OffsetDateTime.now() // Replace with the appropriate time for the forecast item

                dayHour2.text = itemView.context.getString(
                    R.string.daily_hour_second,
                    hourlyForecast.dailyForecast.date.hour
                )
                dailyIcon.setImageResource(
                    ForecastModel.setIcon(
                        hourlyForecast.dailyForecast.weather,
                        time
                    )
                )
                dayGrade.text = itemView.context.getString(
                    R.string.daily_temp_grade,
                    hourlyForecast.dailyForecast.temperature
                )
                dayHumidityGrade.text = itemView.context.getString(
                    R.string.daily_humidity_percent,
                    hourlyForecast.dailyForecast.rainfall
                )
                dailyCardHumidityGrade.text = itemView.context.getString(
                    R.string.daily_humidity_grade,
                    hourlyForecast.dailyForecast.humidity
                )
                dailyCardPrecipitationGrade.text = itemView.context.getString(
                    R.string.daily_precipitation_grade,
                    hourlyForecast.dailyForecast.precip
                )
                dailyCardCoverageGrade.text = itemView.context.getString(
                    R.string.daily_coverage_grade,
                    hourlyForecast.dailyForecast.coverage
                )
                dailyCardRainGrade.text = itemView.context.getString(
                    R.string.daily_rain_grade,
                    hourlyForecast.dailyForecast.rain
                )
                dailyCardWindSpeedGrade.text = itemView.context.getString(
                    R.string.daily_wind_speed,
                    hourlyForecast.dailyForecast.windDirection,
                    hourlyForecast.dailyForecast.wind
                )
                dailyCardUvIndexGrade.text = itemView.context.getString(
                    R.string.daily_cloudCover_grade,
                    hourlyForecast.dailyForecast.index
                )

            }
        }
    }
}