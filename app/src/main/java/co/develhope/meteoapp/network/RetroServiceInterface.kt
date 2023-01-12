package co.develhope.meteoapp.network

import co.develhope.meteoapp.data.dataModel.WeeklyCard
import co.develhope.meteoapp.data.dataTransferObject.DailySummary
import co.develhope.meteoapp.data.dataTransferObject.WeeklySummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServiceInterface {

    @GET("v1/forecast?")
    suspend fun getSummaryForecastWeekly(
        @Query("latitude") latitude                     : Double = 41.7701,
        @Query("longitude") longitude                   : Double = 12.6585,
        @Query("daily") daily                           : List<String> = listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset", "precipitation_sum", "rain_sum", "windspeed_10m_max"),
        @Query("current_weather") current_weather       : Boolean = true,
        @Query("timezone") timezone                     : String = "auto"
    ): Response<WeeklySummary>

    @GET("v1/forecast?")
    suspend fun getHourlyForecastDaily(
        @Query("latitude") latitude                     : Double = 41.7701,
        @Query("longitude") longitude                   : Double = 12.6585,
        @Query("hourly") hourly                         : List<String> = listOf("temperature_2m", "rain", "showers", "snowfall", "weathercode", "windspeed_10m", "relativehumidity_2m", "apparent_temperature", "cloudcover", "winddirection_10m"),
        @Query("current_weather") current_weather       : Boolean = true,
        @Query("timezone") timezone                     : String = "auto",
        @Query("start_date") start_date                 : String,
        @Query("end_date") end_date                     : String
    ): Response<DailySummary>

    @GET("v1/search?")
    suspend fun getWeatherByLocation(
        @Query("latitude") latitude                      : String,
        @Query("longitude") Longitude                    : String
    ): Response<DailySummary>

    @GET("v1/search?")
    suspend fun getWeatherByCityID(
        @Query("id")
        query: String
    ): Response<DailySummary>
}