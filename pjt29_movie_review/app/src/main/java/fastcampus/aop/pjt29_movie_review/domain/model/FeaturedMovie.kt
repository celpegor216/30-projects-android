package fastcampus.aop.pjt29_movie_review.domain.model

data class FeaturedMovie (
    val movie: Movie,
    val latestReview: Review?
)