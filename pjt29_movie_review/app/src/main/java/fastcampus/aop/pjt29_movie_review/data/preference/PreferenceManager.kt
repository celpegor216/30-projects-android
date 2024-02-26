package fastcampus.aop.pjt29_movie_review.data.preference

interface PreferenceManager {

    suspend fun getString(key: String): String?

    suspend fun putString(key: String, value: String)
}