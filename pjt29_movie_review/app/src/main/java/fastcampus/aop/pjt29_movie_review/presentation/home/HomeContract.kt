package fastcampus.aop.pjt29_movie_review.presentation.home

import fastcampus.aop.pjt29_movie_review.domain.model.FeaturedMovie
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.presentation.BasePresenter
import fastcampus.aop.pjt29_movie_review.presentation.BaseView

interface HomeContract {

    interface View: BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovies(
            featuredMovie: FeaturedMovie?,
            movies: List<Movie>
        )

    }

    interface Presenter: BasePresenter
}