package co.develhope.meteoapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.disklrucache.DiskLruCache.Value

class PrefManager(context: Context) {
    var preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = preferences.edit()

    var tempUnit: String get() = preferences.getString(KEY_UNITS, "metric")!!
    set(value) {editor.putString(KEY_UNITS, value).commit()}
}