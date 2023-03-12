package co.develhope.meteoapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.databinding.DailyHourlyItemBinding
import co.develhope.meteoapp.databinding.DailyTitleItemBinding
import co.develhope.meteoapp.model.DailyScreenItems
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.utility.prefs

class DailyScrAdapter(private val newList: List<DailyScreenItems>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HOURLY_TITLE = 0
        const val HOURLY_FORECAST = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HOURLY_TITLE -> DailyTitleViewHolder(
                DailyTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            HOURLY_FORECAST -> DailyHourlyViewHolder(
                DailyHourlyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DailyTitleViewHolder -> holder.bind(newList[position] as DailyScreenItems.Title)
            is DailyHourlyViewHolder -> {
                holder.bind(newList[position] as DailyScreenItems.HourlyForecast)
                // Hide arrow icon for all items except the first one
                if (position != 1) {
                    holder.itemBinding.arrowIcon.visibility = View.GONE
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

    class DailyTitleViewHolder(private val itemBinding: DailyTitleItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(title: DailyScreenItems.Title) {
            itemBinding.dayOfWeek.text = ForecastModel.setDayOfWeek(title.date.dayOfWeek.toString())
            itemBinding.dailyDateTxt.text = itemView.context.getString(
                R.string.daily_date,
                title.date.dayOfWeek.toString()[0] + title.date.dayOfWeek.toString().substring(1).lowercase(),
                title.date.dayOfMonth,
                ForecastModel.setMonthOfYear(title.date.month.toString())
            )
            itemBinding.dayTitle.text = itemView.context.getString(
                R.string.palermo_sic,
                prefs.cityPref,
                prefs.countryPref
            )
            itemBinding.dailyDescriptionTxt.text = ForecastModel.setDescription(title.description)
        }
    }

    class DailyHourlyViewHolder(val itemBinding: DailyHourlyItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hourlyItems: DailyScreenItems.HourlyForecast) {
            itemBinding.dayHour2.text = itemView.context.getString(R.string.daily_hour_second, hourlyItems.dailyForecast.date.hour)
            itemBinding.dailyIcon.setImageResource(ForecastModel.setIcon(hourlyItems.dailyForecast.weather))
            itemBinding.dayGrade.text = itemView.context.getString(
                R.string.daily_temp_grade, hourlyItems.dailyForecast.temperature
            )
            itemBinding.dayHumidityGrade.text = itemView.context.getString(
                R.string.daily_humidity_percent, hourlyItems.dailyForecast.rainfall
            )
            itemBinding.dailyCardHumidityGrade.text = itemView.context.getString(
                R.string.daily_humidity_grade, hourlyItems.dailyForecast.humidity
            )
            itemBinding.dailyCardPrecipitationGrade.text = itemView.context.getString(
                R.string.daily_precipitation_grade, hourlyItems.dailyForecast.precip
            )
            itemBinding.dailyCardCoverageGrade.text = itemView.context.getString(
                R.string.daily_coverage_grade, hourlyItems.dailyForecast.coverage
            )
            itemBinding.dailyCardRainGrade.text = itemView.context.getString(
                R.string.daily_rain_grade, hourlyItems.dailyForecast.rain
            )
            itemBinding.dailyCardWindSpeedGrade.text = itemView.context.getString(
                R.string.daily_wind_speed,
                hourlyItems.dailyForecast.windDirection,
                hourlyItems.dailyForecast.wind
            )
            itemBinding.dailyCardUvIndexGrade.text = itemView.context.getString(
                R.string.daily_cloudCover_grade, hourlyItems.dailyForecast.index
            )
            itemBinding.arrowIcon.setOnClickListener {
                val isExpanded = itemBinding.dailyCardView.visibility == View.VISIBLE
                val rotationAngle = if (isExpanded) 0f else 180f
                itemBinding.arrowIcon.animate().rotation(rotationAngle).start()
                itemBinding.dailyCardView.isVisible = !isExpanded
            }
        }
    }
}

