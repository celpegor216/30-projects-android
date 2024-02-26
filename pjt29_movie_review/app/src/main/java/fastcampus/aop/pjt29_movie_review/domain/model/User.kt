package fastcampus.aop.pjt29_movie_review.domain.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String? = null
)
