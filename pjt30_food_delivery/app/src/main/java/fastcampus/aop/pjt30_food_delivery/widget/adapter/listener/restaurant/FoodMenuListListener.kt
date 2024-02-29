package fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant

import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {

    fun onClickItem(model: FoodModel)
}