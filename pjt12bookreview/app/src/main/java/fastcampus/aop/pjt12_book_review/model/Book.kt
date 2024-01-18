package fastcampus.aop.pjt12_book_review.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize    // 직렬화 가능
data class Book(
    // 서버에서 주는 키("isbn")와 id가 매핑됨
    @SerializedName("isbn") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val coverSmallUrl: String,
    @SerializedName("link") val mobileLink: String
): Parcelable
