package fastcampus.aop.pjt29_movie_review.presentation.mypage

import fastcampus.aop.pjt29_movie_review.domain.model.ReviewedMovie
import fastcampus.aop.pjt29_movie_review.presentation.BasePresenter
import fastcampus.aop.pjt29_movie_review.presentation.BaseView

interface MyPageContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showNoDataDescription(message: String)

        fun showReviewedMovies(reviewedMovies: List<ReviewedMovie>)
    }

    interface Presenter: BasePresenter
}