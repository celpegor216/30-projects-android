package fastcampus.aop.pjt29_movie_review.data.api

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import kotlinx.coroutines.tasks.await

class MovieFirestoreApi(
    private val firestore: FirebaseFirestore
) : MovieApi {

    // Coroutine을 사용하여 addOnCompleteListener 대신 await 사용
    override suspend fun getAllMovies(): List<Movie> =
        firestore.collection("movies")
            .get()
            .await()
            .map { it.toObject<Movie>() }

    override suspend fun getMovies(movieIds: List<String>): List<Movie> =
        firestore.collection("movies")
            .whereIn(FieldPath.documentId(), movieIds)  // "id"로 접근 불가
            .get()
            .await()
            .map { it.toObject<Movie>() }

    companion object {
        const val COLLECTION_MOVIES = "movies"
    }
}