package mahmoudroid.locationreminder.data.source

import android.content.SharedPreferences
import java.lang.Exception

class SharedPref(private val preferences: SharedPreferences) {

    private fun getString(key: String, defaultValue: String = ""): String = preferences.getString(key, defaultValue) ?: defaultValue
    private fun putString(key: String, value: String): Boolean = preferences.edit().putString(key, value).commit()

    private var currentLoc: String = "currentLoc"


    fun getCurrentLoc() = getString(currentLoc, "")
    fun setCurrentLoc(value: String) = putString(currentLoc, value)


    fun clearUserData(){
        try {
            val editor = preferences.edit()
            editor.remove(currentLoc)
            editor.apply()
        }
        catch (e: Exception){}
    }
}