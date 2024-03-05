package fastcampus.aop.pjt30_food_delivery.data

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository

class TestRestaurantFoodRepository: RestaurantFoodRepository {

    private val foodMenuListInBasket = mutableListOf<RestaurantFoodEntity>()
    override suspend fun getFoods(
        restaurantId: Long,
        restaurantTitle: String
    ): List<RestaurantFoodEntity> {
        return listOf<RestaurantFoodEntity>()
    }

    override suspend fun getFoodMenuListInBasket(): List<RestaurantFoodEntity> {
        return foodMenuListInBasket
    }

    override suspend fun getFoodMenuListInBasketByRestaurant(restaurantId: Long): List<RestaurantFoodEntity> {
        return listOf<RestaurantFoodEntity>()
    }

    override suspend fun insertFoodMenuIntoBasket(restaurantFoodEntity: RestaurantFoodEntity) {
        foodMenuListInBasket.add(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuInBasket(foodId: String) {}

    override suspend fun clearFoodMenuListInBasket() {
        foodMenuListInBasket.clear()
    }
}