package co.develhope.meteoapp.utility

import android.content.Context
import android.content.SharedPreferences
import co.develhope.meteoapp.utility.CITY
import co.develhope.meteoapp.utility.COUNTRY
import co.develhope.meteoapp.utility.LATITUDE
import co.develhope.meteoapp.utility.LONGITUDE

class PrefManager(context: Context) {

    private val preferencesLatitude: SharedPreferences = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE)
    private val preferencesLongitude: SharedPreferences = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE)
    private val preferencesCity: SharedPreferences = context.getSharedPreferences(CITY, Context.MODE_PRIVATE)
    private val preferencesCountry: SharedPreferences = context.getSharedPreferences(COUNTRY, Context.MODE_PRIVATE)

    var latitudePref: Float
        get() = preferencesLatitude.getFloat(LATITUDE, 41.8955F)
        set(value) = preferencesLatitude.edit().putFloat(LATITUDE, value).apply()

    var longitudePref: Float
        get() = preferencesLongitude.getFloat(LONGITUDE, 12.4823F)
        set(value) = preferencesLongitude.edit().putFloat(LONGITUDE, value).apply()

    var cityPref: String?
        get() = preferencesCity.getString(CITY, "Rome")
        set(value) = preferencesCity.edit().putString(CITY, value).apply()

    var countryPref: String?
        get() = preferencesCountry.getString(COUNTRY, "Italy")
        set(value) = preferencesCountry.edit().putString(COUNTRY, value).apply()
}
