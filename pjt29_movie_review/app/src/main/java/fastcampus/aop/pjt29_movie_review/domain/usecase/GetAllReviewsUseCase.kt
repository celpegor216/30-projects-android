package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.data.repository.UserRepository
import fastcampus.aop.pjt29_movie_review.domain.model.MovieReviews
import fastcampus.aop.pjt29_movie_review.domain.model.User

class GetAllReviewsUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(movieId: String): MovieReviews {
        val reviews = reviewRepository.getAllMovieReviews(movieId)
        val user = userRepository.getUser()

        if (user == null) {
            userRepository.saveUser(User())

            return MovieReviews(null, reviews)
        }

        return MovieReviews(
            reviews.find { it.userId == user.id },
            reviews.filter { it.userId != user.id }
        )
    }
}