package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.domain.model.Review

class DeleteReviewUseCase(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(review: Review) =
        reviewRepository.deleteReview(review)
}