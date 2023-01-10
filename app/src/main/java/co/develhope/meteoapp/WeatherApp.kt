package co.develhope.meteoapp

import android.app.Application
import android.content.Context

class WeatherApp: Application() {
    companion object {
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }

}