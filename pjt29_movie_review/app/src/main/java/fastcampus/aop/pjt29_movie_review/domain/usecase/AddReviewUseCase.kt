package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.data.repository.UserRepository
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.domain.model.User

class AddReviewUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(
        movie: Movie,
        content: String,
        score: Float
    ): Review {
        var user = userRepository.getUser()

        if (user == null) {
            userRepository.saveUser(User())
            user = userRepository.getUser()
        }

        return reviewRepository.addReview(
            Review(
                userId = user!!.id,
                movieId = movie.id,
                content = content,
                score = score
            )
        )
    }
}