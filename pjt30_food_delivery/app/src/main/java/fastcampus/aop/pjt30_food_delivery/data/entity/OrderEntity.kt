package fastcampus.aop.pjt30_food_delivery.data.entity

data class OrderEntity(
    val id: String,
    val userId: String,
    val restaurantId: Long,
    val restaurantTitle: String,
    val foodMenuList: List<RestaurantFoodEntity>
)