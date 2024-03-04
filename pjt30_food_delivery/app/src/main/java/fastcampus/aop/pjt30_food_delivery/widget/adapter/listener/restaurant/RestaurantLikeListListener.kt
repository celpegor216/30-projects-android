package fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant

import fastcampus.aop.pjt30_food_delivery.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)
}