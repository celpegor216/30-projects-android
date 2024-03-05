package fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.order

import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener

interface OrderListListener: AdapterListener {

    fun writeReview(orderId: String, restaurantTitle: String)
}