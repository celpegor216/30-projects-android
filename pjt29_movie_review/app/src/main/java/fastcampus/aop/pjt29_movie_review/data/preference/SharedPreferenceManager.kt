package fastcampus.aop.pjt29_movie_review.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferenceManager(
    private val sharedPreferences: SharedPreferences
) : PreferenceManager {

    override suspend fun getString(key: String): String? =
        sharedPreferences.getString(key, null)

    override suspend fun putString(key: String, value: String) =
        sharedPreferences.edit { putString(key, value) }
}