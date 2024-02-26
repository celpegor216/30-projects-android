package fastcampus.aop.pjt29_movie_review.data.api

import fastcampus.aop.pjt29_movie_review.domain.model.User

interface UserApi {

    suspend fun saveUser(user: User): User
}