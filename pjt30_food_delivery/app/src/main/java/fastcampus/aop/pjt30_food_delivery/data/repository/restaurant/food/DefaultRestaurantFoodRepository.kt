package fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food

import fastcampus.aop.pjt30_food_delivery.data.db.dao.FoodMenuBasketDao
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService: FoodApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val foodMenuBasketDao: FoodMenuBasketDao
): RestaurantFoodRepository {

    override suspend fun getFoods(restaurantId: Long, restaurantTitle: String): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(restaurantId)

        if (response.isSuccessful) {
            response.body()?.map { it.toEntity(restaurantId, restaurantTitle) } ?: listOf()
        } else {
            listOf()
        }
    }

    override suspend fun getFoodMenuListInBasket(): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        foodMenuBasketDao.getAll()
    }

    override suspend fun getFoodMenuListInBasketByRestaurant(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        foodMenuBasketDao.getAllByRestaurantId(restaurantId)
    }

    override suspend fun insertFoodMenuIntoBasket(restaurantFoodEntity: RestaurantFoodEntity) = withContext(ioDispatcher) {
        foodMenuBasketDao.insert(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuInBasket(foodId: String) = withContext(ioDispatcher) {
        foodMenuBasketDao.delete(foodId)
    }

    override suspend fun clearFoodMenuListInBasket() = withContext(ioDispatcher) {
        foodMenuBasketDao.deleteAll()
    }
}