package fastcampus.aop.pjt30_food_delivery.data.response.restaurant

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String
) {
    fun toEntity(restaurantId: Long) = RestaurantFoodEntity(
        id,
        title,
        description,
        price.toDouble().toInt(),
        imageUrl,
        restaurantId
    )
}
