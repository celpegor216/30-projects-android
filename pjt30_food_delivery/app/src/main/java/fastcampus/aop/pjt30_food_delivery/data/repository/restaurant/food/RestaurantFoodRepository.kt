package fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>
}