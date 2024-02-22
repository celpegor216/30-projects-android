package fastcampus.aop.pjt29_movie_review.domain.model

import com.google.firebase.firestore.DocumentId

data class FeaturedMovie (
    val movie: Movie,
    val latestReview: Review?
)