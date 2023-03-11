package co.develhope.meteoapp.model

import co.develhope.meteoapp.R
import org.threeten.bp.LocalDate
import java.util.*

object ForecastModel {

    private var forecastItems           : WeeklyCard? = null
    private var selectedTodayDetails    : WeeklyCard? = null
    private var selectedTomorrowDetails : WeeklyCard? = null

    fun setIcon(weather: WeatherDescription): Int {
        return when (weather){
            WeatherDescription.CLEAR_SKY       -> R.drawable.ic_sun
            WeatherDescription.FOGGY           -> R.drawable.ic_sun_cloud
            WeatherDescription.PARTLY_CLOUDY   -> R.drawable.ic_sun_cloud
            WeatherDescription.DENSE_INTENSITY -> R.drawable.ic_raining
            WeatherDescription.RAINY           -> R.drawable.ic_raining
            WeatherDescription.HEAVY_INTENSITY -> R.drawable.ic_raining
            WeatherDescription.FREEZING_RAIN   -> R.drawable.ic_raining
            WeatherDescription.SNOW_FALL       -> R.drawable.ic_raining
            WeatherDescription.SNOW_GRAINS     -> R.drawable.ic_raining
            WeatherDescription.RAIN_SHOWERS    -> R.drawable.ic_raining
            WeatherDescription.HEAVY_SNOW      -> R.drawable.ic_raining
            WeatherDescription.THUNDERSTORM    -> R.drawable.ic_raining
            WeatherDescription.HEAVY_HAIL      -> R.drawable.ic_raining

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

    fun setDescription(weatherDescription: WeatherDescription): String {
        return when (weatherDescription) {
            WeatherDescription.CLEAR_SKY       -> "Clear"
            WeatherDescription.FOGGY           -> "Foggy"
            WeatherDescription.PARTLY_CLOUDY   -> "Cloudy"
            WeatherDescription.DENSE_INTENSITY -> "Dense Intensity"
            WeatherDescription.RAINY           -> "Rainy"
            WeatherDescription.HEAVY_INTENSITY -> "Heavy Intensity"
            WeatherDescription.FREEZING_RAIN   -> "Freezing Rain"
            WeatherDescription.SNOW_FALL       -> "Snow Fall"
            WeatherDescription.SNOW_GRAINS     -> "Snow Grains"
            WeatherDescription.RAIN_SHOWERS    -> "Rain Shower"
            WeatherDescription.HEAVY_SNOW      -> "Heavy Snow"
            WeatherDescription.THUNDERSTORM    -> "Thunderstorm"
            WeatherDescription.HEAVY_HAIL      -> "Heavy Hail"
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
    fun changeGeocodingSearchLanguage(): String{
        return when (Locale.getDefault().displayLanguage.lowercase()){
            "english" -> "en"
            "italian" -> "it"
            else -> "en"
        }
    }
}