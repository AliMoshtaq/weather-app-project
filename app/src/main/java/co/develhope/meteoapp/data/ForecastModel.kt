package co.develhope.meteoapp.data

import co.develhope.meteoapp.R
import co.develhope.meteoapp.data.dataModel.*
import org.threeten.bp.LocalDate

object ForecastModel {

    private var forecastItems           : WeeklyCard? = null
    private var selectedTodayDetails    : WeeklyCard? = null
    private var selectedTomorrowDetails : WeeklyCard? = null

    fun setIcon(weather: Weather): Int {
        return when (weather){
            Weather.CLEAR_SKY       -> R.drawable.ic_sun
            Weather.FOGGY           -> R.drawable.ic_sun_cloud
            Weather.PARTLY_CLOUDY   -> R.drawable.ic_sun_cloud
            Weather.DENSE_INTENSITY -> R.drawable.ic_raining
            Weather.RAINY           -> R.drawable.ic_raining
            Weather.HEAVY_INTENSITY -> R.drawable.ic_raining
            Weather.FREEZING_RAIN   -> R.drawable.ic_raining
            Weather.SNOW_FALL       -> R.drawable.ic_raining
            Weather.SNOW_GRAINS     -> R.drawable.ic_raining
            Weather.RAIN_SHOWERS    -> R.drawable.ic_raining
            Weather.HEAVY_SNOW      -> R.drawable.ic_raining
            Weather.THUNDERSTORM    -> R.drawable.ic_raining
            Weather.HEAVY_HAIL      -> R.drawable.ic_raining

        }
    }

    fun setDayOfWeek(dayOfWeek: String): String {
        return when (dayOfWeek) {
            LocalDate.now().dayOfWeek.toString()                    -> "Today"
            LocalDate.now().dayOfWeek.plus(1).toString()      -> "Tomorrow"
            "MONDAY"        -> "Monday"
            "TUESDAY"       -> "Tuesday"
            "WEDNESDAY"     -> "Wednesday"
            "THURSDAY"      -> "Thursday"
            "FRIDAY"        -> "Friday"
            "SATURDAY"      -> "Saturday"
            "SUNDAY"        -> "Sunday"
            else            -> "Unknown"
        }
    }

    fun setDescription(weatherDescription: Weather): String {
        return when (weatherDescription) {
            Weather.CLEAR_SKY       -> "Clear"
            Weather.FOGGY           -> "Foggy"
            Weather.PARTLY_CLOUDY   -> "Cloudy"
            Weather.DENSE_INTENSITY -> "Dense Intensity"
            Weather.RAINY           -> "Rainy"
            Weather.HEAVY_INTENSITY -> "Heavy Intensity"
            Weather.FREEZING_RAIN   -> "Freezing Rain"
            Weather.SNOW_FALL       -> "Snow Fall"
            Weather.SNOW_GRAINS     -> "Snow Grains"
            Weather.RAIN_SHOWERS    -> "Rain Shower"
            Weather.HEAVY_SNOW      -> "Heavy Snow"
            Weather.THUNDERSTORM    -> "Thunderstorm"
            Weather.HEAVY_HAIL      -> "Heavy Hail"
        }
    }

    fun setMonthOfYear(month: String): String {
        return when (month) {
            "JANUARY"       -> "January"
            "FEBRUARY"      -> "February"
            "MARCH"         -> "March"
            "APRIL"         -> "April"
            "MAY"           -> "MAY"
            "JUNE"          -> "June"
            "JULY"          -> "July"
            "AUGUST"        -> "August"
            "SEPTEMBER"     -> "September"
            "OCTOBER"       -> "October"
            "NOVEMBER"      -> "November"
            "DECEMBER"      -> "December"
            else            -> "Unknown"
        }
    }

    fun forecastDetails(forecast: WeeklyCard){
        forecastItems = forecast
    }

    fun getDailyForecastData(): WeeklyCard? = forecastItems


    fun todayDetails(todayDetails: WeeklyCard){
        selectedTodayDetails = todayDetails
    }

    fun tomorrowDetails(tomorrowDetails: WeeklyCard){
        selectedTomorrowDetails = tomorrowDetails
    }

    fun getSelectedDayDetails(position:Int): WeeklyCard? {
        return when(position){
            0       -> selectedTodayDetails
            1       -> selectedTomorrowDetails
            else    -> selectedTodayDetails
        }
    }
}