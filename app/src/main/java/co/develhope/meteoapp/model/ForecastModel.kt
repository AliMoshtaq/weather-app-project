package co.develhope.meteoapp.model

import co.develhope.meteoapp.R
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import java.util.*

object ForecastModel {

    // Private variables for the weekly forecast and selected details
    private var forecastItems           : WeeklyForecast? = null
    private var selectedTodayDetails    : WeeklyForecast? = null
    private var selectedTomorrowDetails : WeeklyForecast? = null

    // Function to set the icon for a given weather description
    fun setIcon(weather: WeatherDescription, time: OffsetDateTime): Int {
        return when (weather) {
            WeatherDescription.CLEAR_SKY ->
                if (isDaytime(time)) R.drawable.ic_sunny else R.drawable.ic_moon
            WeatherDescription.PARTLY_CLOUDY,
            WeatherDescription.CLOUDY           -> R.drawable.ic_partly_cloudy
            WeatherDescription.FOG              -> R.drawable.ic_foggy
            WeatherDescription.LIGHT_RAIN,
            WeatherDescription.MODERATE_RAIN,
            WeatherDescription.HEAVY_RAIN,
            WeatherDescription.LIGHT_SHOWER,
            WeatherDescription.HEAVY_SHOWER     -> R.drawable.ic_rain
            WeatherDescription.FREEZING_RAIN    -> R.drawable.ic_freezing_rain
            WeatherDescription.LIGHT_SNOW,
            WeatherDescription.MODERATE_SNOW,
            WeatherDescription.HEAVY_SNOW       -> R.drawable.ic_snow
            WeatherDescription.SLEET            -> R.drawable.ic_sleet
            WeatherDescription.THUNDERSTORM     -> R.drawable.ic_thunderstorm
            WeatherDescription.UNKNOWN          -> R.drawable.ic_sunny
        }
    }



    // Helper function to determine whether a given time is during the day or night
    private fun isDaytime(time: OffsetDateTime): Boolean {
        val sunrise = time.with(LocalTime.of(6, 0))
        val sunset = time.with(LocalTime.of(18, 0))
        return time.isAfter(sunrise) && time.isBefore(sunset)
    }


    // Function to set the day of the week based on a string input
    fun setDayOfWeek(dayOfWeek: String): String {
        return when (dayOfWeek) {
            // Return the appropriate string for each day of the week
            LocalDate.now().dayOfWeek.toString()                -> "Today"
            LocalDate.now().dayOfWeek.plus(1).toString()   -> "Tomorrow"
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

    // Function to set the description based on a weather description input
    fun setDescription(weatherDescription: WeatherDescription): String {
        return when (weatherDescription) {
            WeatherDescription.CLEAR_SKY       -> "Clear sky"
            WeatherDescription.PARTLY_CLOUDY   -> "Partly cloudy"
            WeatherDescription.CLOUDY          -> "Cloudy"
            WeatherDescription.FOG             -> "Foggy"
            WeatherDescription.LIGHT_RAIN      -> "Light rain"
            WeatherDescription.MODERATE_RAIN   -> "Moderate rain"
            WeatherDescription.HEAVY_RAIN      -> "Heavy rain"
            WeatherDescription.FREEZING_RAIN   -> "Freezing rain"
            WeatherDescription.LIGHT_SNOW      -> "Light snow"
            WeatherDescription.MODERATE_SNOW   -> "Moderate snow"
            WeatherDescription.HEAVY_SNOW      -> "Heavy snow"
            WeatherDescription.SLEET           -> "Sleet"
            WeatherDescription.LIGHT_SHOWER    -> "Light rain shower"
            WeatherDescription.HEAVY_SHOWER    -> "Heavy rain shower"
            WeatherDescription.THUNDERSTORM    -> "Thunderstorm"
            else                               -> "Unknown weather condition"
        }
    }

    // Function to set the month of the year based on a string input
    fun setMonthOfYear(month: String): String {
        return when (month) {
            // Return the appropriate string for each month
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

    // Function to set the weekly forecast details
    fun forecastDetails(forecast: WeeklyForecast){
        forecastItems = forecast
    }

    // Function to get the daily forecast data
    fun getDailyForecastData(): WeeklyForecast? = forecastItems

    // Function to set the selected details for today
    fun todayDetails(todayDetails: WeeklyForecast){
        selectedTodayDetails = todayDetails
    }

    // Function to set the selected details for tomorrow
    fun tomorrowDetails(tomorrowDetails: WeeklyForecast){
        selectedTomorrowDetails = tomorrowDetails
    }

    // Function to get the selected details based on the position
    fun getSelectedDayDetails(position:Int): WeeklyForecast? {
        return when(position){
            // Return the appropriate selected details based on the position
            0       -> selectedTodayDetails
            1       -> selectedTomorrowDetails
            else    -> selectedTodayDetails
        }
    }

    // Function to change the geocoding search language based on the device's default display language
    fun changeGeocodingSearchLanguage(): String{
        return when (Locale.getDefault().displayLanguage.lowercase()){
            // Return the appropriate language code based on the display language
            "english" -> "en"
            "italian" -> "it"
            else -> "en"
        }
    }
}
