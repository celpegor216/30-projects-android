package fastcampus.aop.pjt30_food_delivery.data

import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.order.DefaultOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository

class TestOrderRepository: OrderRepository {

    private var orderEntities = mutableListOf<OrderEntity>()

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        restaurantTitle: String,
        foodMenuList: List<RestaurantFoodEntity>
    ): DefaultOrderRepository.Result {

        orderEntities.add(
            OrderEntity(
                id = orderEntities.size.toString(),
                userId = userId,
                restaurantId = restaurantId,
                foodMenuList = foodMenuList.map { it.copy() },
                restaurantTitle = restaurantTitle
            )
        )

        return DefaultOrderRepository.Result.Success<Any>()
    }

    override suspend fun getAllOrderMenu(userId: String): DefaultOrderRepository.Result {
        return DefaultOrderRepository.Result.Success<Any>(orderEntities)
    }
}