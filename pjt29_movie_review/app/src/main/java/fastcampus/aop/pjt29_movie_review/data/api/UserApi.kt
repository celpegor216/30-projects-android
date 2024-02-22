package fastcampus.aop.pjt29_movie_review.data.api

import fastcampus.aop.pjt29_movie_review.domain.model.Movie

interface MovieApi {

    suspend fun getAllMovies(): List<Movie>
}