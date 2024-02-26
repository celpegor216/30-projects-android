package fastcampus.aop.pjt29_movie_review.domain.model

data class MovieReviews (
    val myReview: Review?,
    val othersReview: List<Review>
)