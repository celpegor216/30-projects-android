package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.MovieRepository
import fastcampus.aop.pjt29_movie_review.domain.model.Movie

class GetAllMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Movie> = movieRepository.getAllMovies()
}