package co.develhope.meteoapp.view.adapter // Define the package name

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.databinding.DailyHourlyItemBinding
import co.develhope.meteoapp.databinding.DailyTitleItemBinding
import co.develhope.meteoapp.model.DailyScreenItems
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.model.ForecastModel.getOrdinalSuffix
import co.develhope.meteoapp.model.ForecastModel.setDescription
import co.develhope.meteoapp.model.getWeatherDescription
import co.develhope.meteoapp.utility.PrefManager
import co.develhope.meteoapp.utility.prefs

class DailyScrAdapter(private val newList: List<DailyScreenItems>) : // Define the adapter class and its input list
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // Extend the RecyclerView.Adapter class

    private var expandedPosition = -1

    companion object { // Define a companion object
        const val HOURLY_TITLE = 0 // Define a constant for the hourly title item type
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
            is DailyTitleViewHolder -> holder.bind(newList[position] as DailyScreenItems.Title, prefs)
            is DailyHourlyViewHolder -> {
                val hourlyForecast = newList[position] as DailyScreenItems.HourlyForecast
                holder.bind(hourlyForecast)

                with(holder.binding) {
                    val currentPosition = holder.getBindingAdapterPosition()
                    arrowIcon.isVisible = currentPosition == 1
                    dailyCardView.isVisible = currentPosition == expandedPosition

                    if (currentPosition == 1) {
                        val isExpanded = currentPosition == expandedPosition
                        val rotationAngle = if (isExpanded) 0f else 180f
                        arrowIcon.rotation = rotationAngle

                        arrowIcon.setOnClickListener {
                            val newExpanded = !dailyCardView.isVisible

                            expandedPosition = if (newExpanded) {
                                val newRotationAngle = 180f
                                arrowIcon.animate().rotation(newRotationAngle).setDuration(200)
                                    .start()
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

    override fun getItemViewType(position: Int): Int {
        return when (newList[position]) {
            is DailyScreenItems.Title -> HOURLY_TITLE
            is DailyScreenItems.HourlyForecast -> HOURLY_FORECAST
        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    class DailyTitleViewHolder(private val binding: DailyTitleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: DailyScreenItems.Title, prefs: PrefManager) {
            with(binding) {
                val dayOfWeekString = title.date.dayOfWeek.toString()
                    .lowercase()
                    .replaceFirstChar { it.uppercase() } // Capitalize the first letter of the day of week string

                val dayDescription = ForecastModel.setDayOfWeek(dayOfWeekString)

                dayOfWeek.text = dayDescription

                val dayOfMonth = title.date.dayOfMonth
                val ordinalSuffix = getOrdinalSuffix(dayOfMonth)
                val dateString = itemView.context.getString(
                    R.string.daily_date,
                    dayOfWeekString,
                    dayOfMonth,
                    ordinalSuffix,
                    ForecastModel.setMonthOfYear(title.date.month.toString())
                )

                val spannableStringBuilder = SpannableStringBuilder(dateString)
                val startIndex = dateString.indexOf(ordinalSuffix)
                val endIndex = startIndex + ordinalSuffix.length
                spannableStringBuilder.setSpan(
                    RelativeSizeSpan(0.7f),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableStringBuilder.setSpan(
                    SuperscriptSpan(),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                dailyDateTxt.text = spannableStringBuilder

                dayTitle.text = itemView.context.getString(
                    R.string.palermo_sic,
                    prefs.cityPref,
                    prefs.countryPref
                )
                // Assuming 'title.weatherCode' is the integer weather code from the API
                val weatherDescription = title.weatherCode.getWeatherDescription()
                val weatherDescriptionString = setDescription(weatherDescription)
                dailyDescriptionTxt.text = weatherDescriptionString
            }
        }
    }





    class DailyHourlyViewHolder(val binding: DailyHourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: DailyScreenItems.HourlyForecast) {
            with(binding) {
                val sunrise = hourlyForecast.dailyForecast.sunrise
                val sunset  = hourlyForecast.dailyForecast.sunset
                val forecastTime = hourlyForecast.dailyForecast.date // Use forecast time instead of the current time
                dayHour2.text = itemView.context.getString(
                    R.string.daily_hour_second,
                    hourlyForecast.dailyForecast.date.hour
                )
                dailyIcon.setImageResource(
                    ForecastModel.setIcon(
                        hourlyForecast.dailyForecast.weather,
                        forecastTime, sunrise, sunset
                    )
                )
                dayGrade.text                       = itemView.context.getString(
                    R.string.daily_temp_grade,
                    hourlyForecast.dailyForecast.temperature
                )
                dayHumidityGrade.text               = itemView.context.getString(
                    R.string.daily_humidity_percent,
                    hourlyForecast.dailyForecast.rainfall
                )
                dailyCardHumidityGrade.text         = itemView.context.getString(
                    R.string.daily_humidity_grade,
                    hourlyForecast.dailyForecast.humidity
                )
                dailyCardPrecipitationGrade.text    = itemView.context.getString(
                    R.string.daily_precipitation_grade,
                    hourlyForecast.dailyForecast.precip
                )
                dailyCardCoverageGrade.text         = itemView.context.getString(
                    R.string.daily_coverage_grade,
                    hourlyForecast.dailyForecast.coverage
                )
                dailyCardRainGrade.text             = itemView.context.getString(
                    R.string.daily_rain_grade,
                    hourlyForecast.dailyForecast.rain
                )
                dailyCardWindSpeedGrade.text        = itemView.context.getString(
                    R.string.daily_wind_speed,
                    hourlyForecast.dailyForecast.windDirection,
                    hourlyForecast.dailyForecast.wind
                )
                dailyCardUvIndexGrade.text          = itemView.context.getString(
                    R.string.daily_cloudCover_grade,
                    hourlyForecast.dailyForecast.index
                )

            }
        }
    }
}