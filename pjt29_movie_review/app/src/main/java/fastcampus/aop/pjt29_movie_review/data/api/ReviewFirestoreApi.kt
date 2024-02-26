package fastcampus.aop.pjt29_movie_review.data.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import fastcampus.aop.pjt29_movie_review.data.api.MovieFirestoreApi.Companion.COLLECTION_MOVIES
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import kotlinx.coroutines.tasks.await

class ReviewFirestoreApi(
    private val firestore: FirebaseFirestore
) : ReviewApi {

    override suspend fun getLatestReview(movieId: String): Review? =
        firestore.collection(COLLECTION_REVIEWS)
            .whereEqualTo(FIELD_MOVIE_ID, movieId)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .map { it.toObject<Review>() }
            .firstOrNull()

    override suspend fun getAllMovieReviews(movieId: String): List<Review> =
        firestore.collection(COLLECTION_REVIEWS)
            .whereEqualTo(FIELD_MOVIE_ID, movieId)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }

    override suspend fun getAllUserReviews(userId: String): List<Review> =
        firestore.collection(COLLECTION_REVIEWS)
            .whereEqualTo(FIELD_USER_ID, userId)
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }

    override suspend fun addReview(review: Review): Review {
        val newReviewReference = firestore.collection(COLLECTION_REVIEWS).document()
        val movieReference = firestore.collection(COLLECTION_MOVIES).document(review.movieId!!)

        // 여러 데이터를 한 번에 조작해야 할 때 오류가 발생하더라도 롤백 가능
        firestore.runTransaction { transaction ->
            val movie = transaction.get(movieReference).toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = oldNumberOfScore + 1
            val newAverageScore = (oldTotalScore + (review.score ?: 0f)) / newNumberOfScore

            transaction.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.set(
                newReviewReference,
                review,
                SetOptions.merge()
            )
        }.await()

        return newReviewReference.get().await().toObject<Review>()!!
    }

    override suspend fun deleteReview(review: Review) {
        val reviewReference = firestore.collection(COLLECTION_REVIEWS).document(review.id!!)
        val movieReference = firestore.collection(COLLECTION_MOVIES).document(review.movieId!!)

        firestore.runTransaction { transaction ->
            val movie = transaction.get(movieReference).toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            // 0 미만이 되지 않도록 처리
            val newNumberOfScore = (oldNumberOfScore - 1).coerceAtLeast(0)
            val newAverageScore = if (newNumberOfScore > 0)
                    (oldTotalScore - (review.score ?: 0f)) / newNumberOfScore
                else
                    0f

            transaction.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.delete(reviewReference)
        }.await()
    }

    companion object {
        private const val COLLECTION_REVIEWS = "reviews"
        private const val FIELD_MOVIE_ID = "movieId"
        private const val FIELD_USER_ID = "userId"
        private const val FIELD_CREATED_AT = "createdAt"
    }
}