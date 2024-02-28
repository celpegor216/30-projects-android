package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>
): BaseViewModel() {

    private val _restaurantMenuListLiveData = MutableLiveData<List<FoodModel>>()
    val restaurantMenuListLiveData: LiveData<List<FoodModel>> = _restaurantMenuListLiveData

    private fun setData(foodList: List<FoodModel>) {
        _restaurantMenuListLiveData.postValue(foodList)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setData(foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId
            )
        })
    }

}