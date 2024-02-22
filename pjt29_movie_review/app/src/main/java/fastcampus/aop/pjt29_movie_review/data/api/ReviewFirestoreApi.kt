package fastcampus.aop.pjt29_movie_review.data.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import kotlinx.coroutines.tasks.await

class MovieFirestoreApi(
    private val firestore: FirebaseFirestore
) : MovieApi {

    override suspend fun getAllMovies(): List<Movie> =
        firestore.collection("movies")
            .get()
            .await()
            .map { it.toObject<Movie>() }
}