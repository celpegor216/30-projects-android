package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    private val _restaurantMenuListLiveData = MutableLiveData<List<FoodModel>>()
    val restaurantMenuListLiveData: LiveData<List<FoodModel>> = _restaurantMenuListLiveData

    private val _menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()
    val menuBasketLiveData: LiveData<RestaurantFoodEntity> = _menuBasketLiveData

    private val _isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean, () -> Unit>>()
    val isClearNeedInBasketLiveData: LiveData<Pair<Boolean, () -> Unit>> = _isClearNeedInBasketLiveData

    private fun setRestaurantMenuList(foodList: List<FoodModel>) {
        _restaurantMenuListLiveData.postValue(foodList)
    }

    private fun setMenuBasket(restaurantFoodEntity: RestaurantFoodEntity) {
        _menuBasketLiveData.postValue(restaurantFoodEntity)
    }

    private fun setState(state: Pair<Boolean, () -> Unit>) {
        _isClearNeedInBasketLiveData.postValue(state)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setRestaurantMenuList(foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId,
                foodId = it.id
            )
        })
    }

    fun insertMenuIntoBasket(model: FoodModel) = viewModelScope.launch {
        val restaurantMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasketByRestaurant(restaurantId)
        val foodMenuEntity = model.toEntity(restaurantMenuListInBasket.size)
        val anotherRestaurantMenuListInBasket =
            restaurantFoodRepository.getFoodMenuListInBasket().filter { it.restaurantId != restaurantId }
        if (anotherRestaurantMenuListInBasket.isNotEmpty()) {
            setState(Pair(true) { clearMenuAndInsertNewMenuIntoBasket(foodMenuEntity) })
        } else {
            restaurantFoodRepository.insertFoodMenuIntoBasket(foodMenuEntity)
            setMenuBasket(foodMenuEntity)
        }
    }

    private fun clearMenuAndInsertNewMenuIntoBasket(foodMenuEntity: RestaurantFoodEntity) = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        restaurantFoodRepository.insertFoodMenuIntoBasket(foodMenuEntity)
        setMenuBasket(foodMenuEntity)
    }

}