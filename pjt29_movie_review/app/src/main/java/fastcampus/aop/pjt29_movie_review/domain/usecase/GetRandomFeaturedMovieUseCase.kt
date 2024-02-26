package fastcampus.aop.pjt29_movie_review.domain.usecase

import fastcampus.aop.pjt29_movie_review.data.repository.MovieRepository
import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.domain.model.FeaturedMovie

class GetRandomFeaturedMovieUseCase(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(): FeaturedMovie? {
        val featuredMovies = movieRepository.getAllMovies()
            .filter { it.id.isNullOrBlank().not() }
            .filter { it.isFeatured == true }

        if (featuredMovies.isNullOrEmpty()) {
            return null
        }

        return featuredMovies.random().let { movie ->
            val latestReview = reviewRepository.getLatestReview(movie.id!!)
            FeaturedMovie(movie, latestReview)
        }
    }
}