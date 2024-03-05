package fastcampus.aop.pjt30_food_delivery.model.restaurant.review

import android.net.Uri
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Float,
    val thumbnailImageUri: Uri? = null
): Model(id, type)