package fastcampus.aop.pjt30_food_delivery.model.restaurant.food

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val restaurantTitle: String,
    val foodId: String
) : Model(id, type) {

    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}", title, description, price, imageUrl, restaurantId, restaurantTitle
    )
}
