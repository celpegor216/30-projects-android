package fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>

    suspend fun getFoodMenuListInBasket(): List<RestaurantFoodEntity>

    suspend fun getFoodMenuListInBasketByRestaurant(restaurantId: Long): List<RestaurantFoodEntity>

    suspend fun insertFoodMenuIntoBasket(restaurantFoodEntity: RestaurantFoodEntity)

    suspend fun removeFoodMenuInBasket(foodId: String)

    suspend fun clearFoodMenuListInBasket()
}