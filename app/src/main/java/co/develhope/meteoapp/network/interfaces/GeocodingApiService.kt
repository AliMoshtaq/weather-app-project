package co.develhope.meteoapp.network.interfaces

import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.model.dto.GeocodingDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("v1/search?")
    suspend fun getLocation(
        @Query("name") name: String? = null,
        @Query("language") language: String = ForecastModel.changeGeocodingSearchLanguage()
        //@Query("count") count: Int = 5
    ): Response<GeocodingDTO>
}