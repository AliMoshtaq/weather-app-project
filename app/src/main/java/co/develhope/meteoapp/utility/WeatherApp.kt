package co.develhope.meteoapp.utility

import android.app.Application


val prefs: PrefManager by lazy {
    WeatherApp.prefs!!
}

class WeatherApp : Application() {
    companion object {
        var prefs: PrefManager? = null
            private set
        lateinit var instance: WeatherApp
            private set

    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = PrefManager(applicationContext)

    }
}