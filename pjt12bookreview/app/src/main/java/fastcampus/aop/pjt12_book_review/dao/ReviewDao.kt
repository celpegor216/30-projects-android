package fastcampus.aop.pjt12_book_review.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fastcampus.aop.pjt12_book_review.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE isbn = :isbn")
    fun getReview(isbn: String): Review

    // 같은 값이 있을 경우 중복 생성이 아닌 교체
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}