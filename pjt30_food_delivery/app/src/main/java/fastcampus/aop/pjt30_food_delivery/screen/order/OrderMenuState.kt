package fastcampus.aop.pjt30_food_delivery.screen.order

import androidx.annotation.StringRes
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel

sealed class OrderMenuState {

    object Uninitialized: OrderMenuState()

    object Loading: OrderMenuState()

    object Order: OrderMenuState()

    data class Success(
        val restaurantFoodModelList: List<FoodModel>?
    ): OrderMenuState()

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): OrderMenuState()

}