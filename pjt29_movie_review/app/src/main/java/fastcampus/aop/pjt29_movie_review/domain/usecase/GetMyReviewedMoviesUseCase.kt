package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.MovieRepository
import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.data.repository.UserRepository
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.domain.model.ReviewedMovie
import fastcampus.aop.pjt29_movie_review.domain.model.User

class GetMyReviewedMoviesUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): List<ReviewedMovie> {
        val user = userRepository.getUser()
        if (user == null) {
            userRepository.saveUser(User())
            return emptyList()
        }

        val reviews = reviewRepository.getAllUserReviews(user.id!!)
            .filter { it.movieId.isNullOrBlank().not() }

        if (reviews.isNullOrEmpty()) {
            return emptyList()
        }

        return movieRepository.getMovies(reviews.map { it.movieId!! })
            .mapNotNull { movie ->
                val relatedReview = reviews.find { it.movieId == movie.id }
                relatedReview?.let { ReviewedMovie(movie, it) }
            }
    }
}