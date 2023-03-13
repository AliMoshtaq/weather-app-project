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
        return when (weather){
            // Return the appropriate resource ID for each type of weather
//            WeatherDescription.CLEAR_SKY       -> R.drawable.ic_sun
//            WeatherDescription.FOGGY           -> R.drawable.ic_sun_cloud
//            WeatherDescription.PARTLY_CLOUDY   -> R.drawable.ic_sun_cloud
//            WeatherDescription.DENSE_INTENSITY -> R.drawable.ic_raining
//            WeatherDescription.RAINY           -> R.drawable.ic_raining
//            WeatherDescription.HEAVY_INTENSITY -> R.drawable.ic_raining
//            WeatherDescription.FREEZING_RAIN   -> R.drawable.ic_raining
//            WeatherDescription.SNOW_FALL       -> R.drawable.ic_raining
//            WeatherDescription.SNOW_GRAINS     -> R.drawable.ic_raining
//            WeatherDescription.RAIN_SHOWERS    -> R.drawable.ic_raining
//            WeatherDescription.HEAVY_SNOW      -> R.drawable.ic_raining
//            WeatherDescription.THUNDERSTORM    -> R.drawable.ic_raining
//            WeatherDescription.HEAVY_HAIL      -> R.drawable.ic_raining
            WeatherDescription.CLEAR_SKY -> if (isDaytime(time)) R.drawable.ic_sun else R.drawable.ic_fluent_moon
            WeatherDescription.FOGGY, WeatherDescription.PARTLY_CLOUDY, WeatherDescription.RAINY,
            WeatherDescription.DENSE_INTENSITY, WeatherDescription.FREEZING_RAIN, WeatherDescription.SNOW_FALL,
            WeatherDescription.SNOW_GRAINS, WeatherDescription.RAIN_SHOWERS, WeatherDescription.HEAVY_SNOW,
            WeatherDescription.THUNDERSTORM, WeatherDescription.HEAVY_HAIL, WeatherDescription.HEAVY_INTENSITY ->
                R.drawable.ic_raining
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
            LocalDate.now().dayOfWeek.toString()            -> "Today"
            LocalDate.now().dayOfWeek.plus(1).toString()     -> "Tomorrow"
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
            // Return the appropriate string for each type of weather
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
