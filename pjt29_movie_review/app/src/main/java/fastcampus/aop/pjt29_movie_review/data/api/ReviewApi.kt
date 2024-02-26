package fastcampus.aop.pjt29_movie_review.data.api

import fastcampus.aop.pjt29_movie_review.domain.model.Review

interface ReviewApi {

    suspend fun getLatestReview(movieId: String): Review?

    suspend fun getAllMovieReviews(movieId: String): List<Review>

    suspend fun getAllUserReviews(userId: String): List<Review>


    suspend fun addReview(review: Review): Review

    suspend fun deleteReview(review: Review)
}