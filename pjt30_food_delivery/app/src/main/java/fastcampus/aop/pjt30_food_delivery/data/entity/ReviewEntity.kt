package fastcampus.aop.pjt30_food_delivery.data.entity

data class ReviewEntity(
    val userId: String,
    val createdAt: Long,
    val title: String,
    val content: String,
    val rating: Float,
    val imageUrlList: List<String>?,
    val orderId: String,
    val restaurantTitle: String
)