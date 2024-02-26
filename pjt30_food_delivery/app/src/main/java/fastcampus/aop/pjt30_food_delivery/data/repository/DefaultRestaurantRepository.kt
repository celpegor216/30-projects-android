package fastcampus.aop.pjt30_food_delivery.data.repository

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantRepository(
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun getList(restaurantCategory: RestaurantCategory): List<RestaurantEntity> =
        withContext(ioDispatcher) {
            // TODO: API를 통한 데이터 받아오기

            listOf(
                RestaurantEntity(
                    id = 0,
                    restaurantInfoId = 0,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                ),

                RestaurantEntity(
                    id = 1,
                    restaurantInfoId = 1,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집1",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                ),
                RestaurantEntity(
                    id = 2,
                    restaurantInfoId = 2,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집2",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                ),
                RestaurantEntity(
                    id = 3,
                    restaurantInfoId = 3,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집3",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                ),
                RestaurantEntity(
                    id = 4,
                    restaurantInfoId = 4,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집4",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                ),
                RestaurantEntity(
                    id = 5,
                    restaurantInfoId = 5,
                    restaurantCategory = RestaurantCategory.ALL,
                    restaurantTitle = "마포화로집5",
                    restaurantImageUrl = "https://picsum.photos/200",
                    grade = (1 until 5).random() + ((0..10).random() / 10f),
                    reviewCount = (0 until 200).random(),
                    deliveryTimeRange = Pair(0, 20),
                    deliveryTipRange = Pair(0, 2000),
                )
            )
        }
}