package fastcampus.aop.pjt12_book_review.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey val isbn: String,
    @ColumnInfo("review") val review: String?
)
