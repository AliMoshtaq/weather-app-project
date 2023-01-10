package co.develhope.meteoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import co.develhope.meteoapp.data.dataModel.CityUpdate
import co.develhope.meteoapp.data.dataModel.Cities

@Dao
interface CityDao {

    @Query("SELECT * FROM city_db WHERE name LIKE :key || '%'")
    suspend fun searchCity(key: String): List<Cities>

    @Update(entity = Cities::class)
    suspend fun updateSavedCity(vararg obj: CityUpdate):Int

    @Query("SELECT * FROM city_db WHERE isSaved= :key")
    fun getSavedCity(key:Int): LiveData<List<Cities>>

    @Delete
    suspend fun deleteSavedCity(city:Cities)


}