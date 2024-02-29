package fastcampus.aop.pjt30_food_delivery.data.preference

import android.content.Context
import android.content.SharedPreferences

class AppPreferenceManager(
    private val context: Context
) {

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
    private val prefs by lazy { getPreferences(context) }
    private val editor by lazy { prefs.edit() }

    fun putIdToken(idToken: String) {
        editor.putString(KEY_ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(): String? {
        return prefs.getString(KEY_ID_TOKEN, null)
    }

    fun removeIdToken() {
        editor.putString(KEY_ID_TOKEN, null)
        editor.apply()
    }

    companion object {
        const val PREFERENCES_NAME = "pjt30_food_delivery"
        const val KEY_ID_TOKEN = "ID_TOKEN"
    }
}