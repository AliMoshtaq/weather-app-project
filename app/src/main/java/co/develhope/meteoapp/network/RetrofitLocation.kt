package co.develhope.meteoapp.network

import co.develhope.meteoapp.interfaces.RetroServiceInterface
import co.develhope.meteoapp.utility.LOC_BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitLocationInstance {

    companion object {


        private fun getRetrofitInstance(client: OkHttpClient, gson: Gson): Retrofit =
            Retrofit.Builder()
                .baseUrl(LOC_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        private fun getGson(): Gson = GsonBuilder().create()

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

        val apiService: RetroServiceInterface by lazy {
            retrofit.create(RetroServiceInterface::class.java)
        }
    }
}
