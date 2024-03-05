package fastcampus.aop.pjt30_food_delivery.data.entity

import android.net.Uri

data class GalleryPhoto(
    val id: Long,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    val isSelected: Boolean = false
)