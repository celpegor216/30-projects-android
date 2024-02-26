package fastcampus.aop.pjt29_movie_review.presentation.home

import fastcampus.aop.pjt29_movie_review.domain.usecase.GetAllMoviesUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetRandomFeaturedMovieUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeContract.View,
    private val getRandomFeaturedMovie: GetRandomFeaturedMovieUseCase,
    private val getAllMovies: GetAllMoviesUseCase
): HomeContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchMovies()
    }

    override fun onDestroyView() {}

    private fun fetchMovies() = scope.launch {
        try {
            view.showLoadingIndicator()

            val featuredMovie = getRandomFeaturedMovie()
            val movies = getAllMovies()

            view.showMovies(featuredMovie, movies)
        } catch (e: Exception) {
            e.printStackTrace()

            view.showErrorDescription("오류가 발생했습니다.")
        } finally {
            view.hideLoadingIndicator()
        }
    }

}