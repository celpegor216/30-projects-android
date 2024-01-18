package fastcampus.aop.pjt12_book_review.model

import com.google.gson.annotations.SerializedName

// dto: 전체 모델에서 데이터를 꺼내올 수 있게 연결
data class SearchBooksDto(
    @SerializedName("items") val books: List<Book>
)
