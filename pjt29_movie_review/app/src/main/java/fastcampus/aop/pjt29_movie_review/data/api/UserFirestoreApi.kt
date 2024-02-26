package fastcampus.aop.pjt29_movie_review.data.api

import com.google.firebase.firestore.FirebaseFirestore
import fastcampus.aop.pjt29_movie_review.domain.model.User
import kotlinx.coroutines.tasks.await

class UserFirestoreApi(
    private val firestore: FirebaseFirestore
) : UserApi {

    override suspend fun saveUser(user: User): User = firestore.collection(COLLECTION_USERS)
        .add(user)
        .await()
        .let { User(it.id) }

    companion object {
        private const val COLLECTION_USERS = "users"
    }
}