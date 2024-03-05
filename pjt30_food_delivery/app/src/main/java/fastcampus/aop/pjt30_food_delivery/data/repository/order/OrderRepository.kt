package fastcampus.aop.pjt30_food_delivery.data.repository.order

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        restaurantTitle: String,
        foodMenuList: List<RestaurantFoodEntity>
    ): DefaultOrderRepository.Result

    suspend fun getAllOrderMenu(userId: String): DefaultOrderRepository.Result
}