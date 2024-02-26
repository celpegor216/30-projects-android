package fastcampus.aop.pjt29_movie_review.presentation.reviews

import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.MovieReviews
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.domain.usecase.AddReviewUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.DeleteReviewUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetAllReviewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ReviewsPresenter(
    private val view: ReviewsContract.View,
    override val movie: Movie,
    private val getAllReviews: GetAllReviewsUseCase,
    private val addReview: AddReviewUseCase,
    private val deleteReview: DeleteReviewUseCase
) : ReviewsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    private var movieReviews: MovieReviews = MovieReviews(null, emptyList())

    override fun onViewCreated() {
        view.showMovieInformation(movie)
        fetchReviews()
    }

    override fun onDestroyView() {}

    override fun requestAddReview(content: String, score: Float) {
        scope.launch {
            try {
                view.showLoadingIndicator()

                val submittedReview = addReview(movie, content, score)
                view.showReviews(movieReviews.copy(myReview = submittedReview))
            } catch (e: Exception) {
                e.printStackTrace()
                view.showErrorToast("리뷰 등록에 실패했습니다.")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    override fun requestDeleteReview(review: Review) {
        scope.launch {
            try {
                view.showLoadingIndicator()

                deleteReview(review)
                view.showReviews(movieReviews.copy(myReview = null))
            } catch (e: Exception) {
                e.printStackTrace()
                view.showErrorToast("리뷰 삭제에 실패했습니다.")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    private fun fetchReviews() = scope.launch {
        try {
            view.showLoadingIndicator()
            view.showReviews(getAllReviews(movie.id!!))
        } catch (e: Exception) {
            e.printStackTrace()
            view.showErrorDescription("에러가 발생했습니다.")
        } finally {
            view.hideLoadingIndicator()
        }
    }
}