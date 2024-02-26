package fastcampus.aop.pjt29_movie_review.data.repository

import fastcampus.aop.pjt29_movie_review.data.api.ReviewApi
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ReviewRepositoryImpl(
    private val reviewApi: ReviewApi,
    private val dispatcher: CoroutineDispatcher
) : ReviewRepository {

    override suspend fun getLatestReview(movieId: String): Review? =
        withContext(dispatcher) {
            reviewApi.getLatestReview(movieId)
        }

    override suspend fun getAllMovieReviews(movieId: String): List<Review> =
        withContext(dispatcher) {
            reviewApi.getAllMovieReviews(movieId)
        }

    override suspend fun getAllUserReviews(userId: String): List<Review> =
        withContext(dispatcher) {
            reviewApi.getAllUserReviews(userId)
        }

    override suspend fun addReview(review: Review): Review = withContext(dispatcher) {
        reviewApi.addReview(review)
    }

    override suspend fun deleteReview(review: Review) = withContext(dispatcher) {
        reviewApi.deleteReview(review)
    }
}