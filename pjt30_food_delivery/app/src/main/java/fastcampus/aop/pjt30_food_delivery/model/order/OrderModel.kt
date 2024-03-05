package fastcampus.aop.pjt30_food_delivery.model.order

import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val restaurantTitle: String,
    val foodMenuList: List<RestaurantFoodEntity>
) : Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        restaurantTitle = restaurantTitle,
        foodMenuList = foodMenuList
    )
}
