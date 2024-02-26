package fastcampus.aop.pjt29_movie_review.data.repository

import fastcampus.aop.pjt29_movie_review.domain.model.Movie

interface MovieRepository {

    suspend fun getAllMovies(): List<Movie>

    suspend fun getMovies(movieIds: List<String>): List<Movie>
}