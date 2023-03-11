package co.develhope.meteoapp.network

import co.develhope.meteoapp.network.interfaces.RetroServiceInterface
import co.develhope.meteoapp.utility.OffsetDateTimeCustomAdapter
import co.develhope.meteoapp.model.DailyForecast
import co.develhope.meteoapp.model.WeeklyCard
import co.develhope.meteoapp.utility.FORECAST_BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {

        private fun getRetrofitInstance(client: OkHttpClient, gson: Gson): Retrofit =
            Retrofit.Builder()
                .baseUrl(FORECAST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        private fun getGson(): Gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeCustomAdapter())
            .create()

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

        private fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).build()
        }

        private val retrofit = getRetrofitInstance(
            client = getOkHttpClient(loggingInterceptor = getHttpLoggingInterceptor()),
            gson = getGson()
        )

        private val apiService: RetroServiceInterface =
            retrofit.create(RetroServiceInterface::class.java)

        suspend fun getForecastSummary(): List<WeeklyCard> {
            return apiService.getSummaryForecastWeekly().body()?.daily?.mapToDomain()?: emptyList()
        }

        suspend fun getDailyForecast(startDate:String, endDate:String): List<DailyForecast> {
            return apiService.getHourlyForecastDaily(start_date = startDate, end_date = endDate).body()?.hourly?.mapToDomain()?: emptyList()
        }
    }
}
