package co.develhope.meteoapp

import android.app.Application
import android.content.Context
import co.develhope.meteoapp.utility.PrefManager

val prefs: PrefManager by lazy {
    WeatherApp.prefs!!
}

class WeatherApp: Application() {

    companion object {
        var prefs: PrefManager? = null
        lateinit var instance: WeatherApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = PrefManager(applicationContext)
    }
}