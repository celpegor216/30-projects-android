package fastcampus.aop.pjt29_movie_review.presentation.reviews

import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.presentation.BasePresenter
import fastcampus.aop.pjt29_movie_review.presentation.BaseView

interface ReviewContract {

    interface View: BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovieInformation(movie: Movie)

        fun showReviews(reviews: List<Review>)
    }

    interface Presenter: BasePresenter {
        val movie: Movie
    }
}