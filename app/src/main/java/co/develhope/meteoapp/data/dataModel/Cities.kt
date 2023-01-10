package co.develhope.meteoapp.data.dataModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.develhope.meteoapp.utils.TABLE_CITY


@Entity(
    tableName = TABLE_CITY
)
data class Cities(
    @PrimaryKey(autoGenerate = false)
    val id          : Int,
    @ColumnInfo(name = "name")
    val city        : String,
    @ColumnInfo(name = "region")
    val region      : String,
    @ColumnInfo(name = "latitude")
    val latitude    : Double,
    @ColumnInfo(name = "longitude")
    val longitude   : Double,
    @ColumnInfo(name = "isSaved")
    val isSaved     : Int,
    @ColumnInfo(name = "temp")
    val temp        : Int,
    @ColumnInfo(name = "description")
    val description : String
)
