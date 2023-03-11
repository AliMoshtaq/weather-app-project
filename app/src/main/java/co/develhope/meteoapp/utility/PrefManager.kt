package co.develhope.meteoapp.utility

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    var tempUnit: String get() = preferences.getString(KEY_UNITS, "metric")!!
    set(value) {editor.putString(KEY_UNITS, value).commit()}
}