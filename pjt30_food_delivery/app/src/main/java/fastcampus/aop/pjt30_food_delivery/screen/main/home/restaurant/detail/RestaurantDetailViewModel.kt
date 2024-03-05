package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    private val _restaurantDetailStateLiveData =
        MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)
    val restaurantDetailStateLiveData: LiveData<RestaurantDetailState> =
        _restaurantDetailStateLiveData

    private fun setState(state: RestaurantDetailState) {
        _restaurantDetailStateLiveData.postValue(state)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(RestaurantDetailState.Loading)

        val foods = restaurantFoodRepository.getFoods(
            restaurantEntity.restaurantInfoId,
            restaurantEntity.restaurantTitle
        )
        val foodMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket()
        val isLiked =
            userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        setState(
            RestaurantDetailState.Success(
                restaurantEntity = restaurantEntity,
                restaurantFoodList = foods,
                foodMenuListInBasket = foodMenuListInBasket,
                isLiked = isLiked
            )
        )
    }

    fun getRestaurantTelNumber(): String? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }

            else -> null
        }
    }

    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }

            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(data.restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    setState(
                        data.copy(
                            isLiked = false
                        )
                    )
                } ?: run {
                    userRepository.insertUserLikedRestaurant(data.restaurantEntity)
                    setState(
                        data.copy(
                            isLiked = true
                        )
                    )
                }
            }

            else -> null
        }
    }

    fun notifyFoodMenuListInBasketChanged(restaurantFoodEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            when (val data = restaurantDetailStateLiveData.value) {
                is RestaurantDetailState.Success -> {
                    setState(data.copy(
                        foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                            add(restaurantFoodEntity)
                        }
                    ))
                }

                else -> Unit
            }
        }

    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) =
        viewModelScope.launch {
            when (val data = restaurantDetailStateLiveData.value) {
                is RestaurantDetailState.Success -> {
                    setState(
                        data.copy(
                            isClearNeedInBasketAndAction = Pair(clearNeed, afterAction)
                        )
                    )
                }

                else -> Unit
            }
        }

    fun notifyClearBasket() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                setState(data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false) {}
                ))
            }

            else -> Unit
        }
    }

    fun checkMyBasket() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                setState(
                    data.copy(
                        foodMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket(),
                    )
                )
            }

            else -> Unit
        }
    }
}