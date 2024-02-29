package fastcampus.aop.pjt30_food_delivery.data.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantReviewEntity(
    override val id: Long,
    val title: String,
    val description: String,
    val grade: Int,
    val images: List<Uri>? = null
) : Entity, Parcelable