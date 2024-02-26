package fastcampus.aop.pjt29_movie_review.data.repository

import fastcampus.aop.pjt29_movie_review.data.api.MovieApi
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispatcher: CoroutineDispatcher
): MovieRepository {

    override suspend fun getAllMovies(): List<Movie> = withContext(dispatcher) {
        movieApi.getAllMovies()
    }

    override suspend fun getMovies(movieIds: List<String>): List<Movie> = withContext(dispatcher) {
        movieApi.getMovies(movieIds)
    }
}