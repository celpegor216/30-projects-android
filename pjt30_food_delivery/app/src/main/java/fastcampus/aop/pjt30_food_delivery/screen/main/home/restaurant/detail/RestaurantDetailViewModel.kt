package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
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

        val foods = restaurantFoodRepository.getFoods(restaurantEntity.restaurantInfoId)
        val isLiked =
            userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        setState(
            RestaurantDetailState.Success(
                restaurantEntity = restaurantEntity,
                restaurantFoodList = foods,
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
}