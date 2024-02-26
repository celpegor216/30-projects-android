package fastcampus.aop.pjt29_movie_review.presentation.reviews

import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.MovieReviews
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.presentation.BasePresenter
import fastcampus.aop.pjt29_movie_review.presentation.BaseView

interface ReviewsContract {

    interface View: BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovieInformation(movie: Movie)

        fun showReviews(movieReviews: MovieReviews)

        fun showErrorToast(message: String)
    }

    interface Presenter: BasePresenter {
        val movie: Movie

        fun requestAddReview(content: String, score: Float)

        fun requestDeleteReview(review: Review)
    }
}