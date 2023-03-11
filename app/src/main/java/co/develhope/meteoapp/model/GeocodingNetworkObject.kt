package co.develhope.meteoapp.model

import co.develhope.meteoapp.network.interfaces.GeocodingApiService
import co.develhope.meteoapp.utility.LOC_BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GeocodingNetworkObject {
    private fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOC_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    private fun provideGson(): Gson = GsonBuilder()
        .create()


    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit = provideRetrofit(
        client = provideOkHttpClient(loggingInterceptor = provideHttpLoggingInterceptor()),
        gson = provideGson()
    )

    private val service = retrofit.create(GeocodingApiService::class.java)

    suspend fun getLocationInfo(city: String): List<LocationData> {
        return service.getLocation(city)
            .body()?.resultsDTO?.map { resultDTO ->
                resultDTO.toDomain()
            }
            ?: emptyList()
    }
}