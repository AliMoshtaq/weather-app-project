package co.develhope.meteoapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.meteoapp.R
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.model.HomeScreenItem
import co.develhope.meteoapp.databinding.HomeForecastItemBinding
import co.develhope.meteoapp.databinding.HomeSubtitleItemBinding
import co.develhope.meteoapp.databinding.HomeTitleItemBinding
import co.develhope.meteoapp.network.interfaces.OnItemClickListener
import co.develhope.meteoapp.utility.prefs
import org.threeten.bp.OffsetDateTime

class HomeScrAdapter(
    private val newList: List<HomeScreenItem>,
    private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TITLE     = 0
        const val CARD      = 1
        const val SUBTITLE  = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (newList[position]) {
            is HomeScreenItem.Forecast  -> CARD
            is HomeScreenItem.Subtitle  -> SUBTITLE
            is HomeScreenItem.Title     -> TITLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE       -> TitleViewHolder(
                HomeTitleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
            CARD        -> ForecastViewHolder(
                HomeForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            SUBTITLE    -> SubTitleViewHolder(
                HomeSubtitleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else        -> throw java.lang.IllegalArgumentException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder    -> holder.bind(newList[position] as HomeScreenItem.Title)
            is ForecastViewHolder -> holder.bind(newList[position] as HomeScreenItem.Forecast,clickListener)
            is SubTitleViewHolder -> holder.bind(newList[position] as HomeScreenItem.Subtitle)
        }
    }

    override fun getItemCount(): Int = newList.size

    class TitleViewHolder(private val binding: HomeTitleItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(titleItem: HomeScreenItem.Title) {
            binding.titleTv.text = itemView.context.getString(
                R.string.palermo_sic,
                prefs.cityPref,
                prefs.countryPref
            )
        }
    }

    class ForecastViewHolder(private val binding: HomeForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastItem: HomeScreenItem.Forecast, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.viewDailyScreen(forecastItem,forecastItem.weeklyForecast.date)
            }
            binding.tvDate.text             = itemView.context.getString(R.string.tv_date,
                forecastItem.weeklyForecast.date.dayOfMonth,
                forecastItem.weeklyForecast.date.monthValue)
            binding.tvGradeMax.text         = itemView.context.getString(R.string.tv_grade_max,
                forecastItem.weeklyForecast.maxTemp)
            binding.tvGradeMin.text         = itemView.context.getString(R.string.tv_grade_min,
                forecastItem.weeklyForecast.minTemp)
            binding.tvPrecipitation.text    = itemView.context.getString(R.string.tv_precip_num,
                forecastItem.weeklyForecast.precipitation)
            binding.tvSpeed.text            = itemView.context.getString(R.string.tv_kmh,
                forecastItem.weeklyForecast.wind)
            binding.tvToday.text            = ForecastModel.setDayOfWeek(forecastItem.weeklyForecast.date.dayOfWeek.name)
            val time = OffsetDateTime.now() // Replace with the appropriate time for the forecast item
            binding.icCloudy.setImageResource(ForecastModel.setIcon(forecastItem.weeklyForecast.weather, time))

        }
    }

    class SubTitleViewHolder(private val binding: HomeSubtitleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subtitleItem: HomeScreenItem.Subtitle) {
            binding.tvSubtitle.text = itemView.context.getString(
                R.string.next_five_days,
                subtitleItem.subTitle
            )
        }
    }
}