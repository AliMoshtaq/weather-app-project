package co.develhope.meteoapp.network

import co.develhope.meteoapp.model.LocationData
import co.develhope.meteoapp.network.interfaces.GeocodingApiService
import co.develhope.meteoapp.utility.LOC_BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GeocodingNetworkModel {
    private val service: GeocodingApiService
    init {
        val gson = GsonBuilder().create()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(LOC_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        service = retrofit.create(GeocodingApiService::class.java)
    }

    suspend fun getLocationInfo(city: String): List<LocationData> {
        return service.getLocation(city)
            .body()?.resultsDTO?.map { resultDTO ->
                resultDTO.toDomain()
            }
            ?: emptyList()
    }
}
