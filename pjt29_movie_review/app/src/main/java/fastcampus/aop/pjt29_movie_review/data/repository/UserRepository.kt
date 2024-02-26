package fastcampus.aop.pjt29_movie_review.data.repository

import fastcampus.aop.pjt29_movie_review.domain.model.User


interface UserRepository {

    suspend fun getUser(): User?

    suspend fun saveUser(user: User)
}