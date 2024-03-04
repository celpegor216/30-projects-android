package fastcampus.aop.pjt30_food_delivery.screen.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.repository.order.DefaultOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _orderMenuStateLiveData =
        MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)
    val orderMenuStateLiveData: LiveData<OrderMenuState> = _orderMenuStateLiveData

    private fun setState(state: OrderMenuState) {
        _orderMenuStateLiveData.postValue(state)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(OrderMenuState.Loading)
        val foodMenuList = restaurantFoodRepository.getFoodMenuListInBasket()

        setState(OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id
                )
            }
        ))
    }

    fun orderMenu() = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getFoodMenuListInBasket()
        if (foodMenuList.isNotEmpty()) {
            val restaurantId = foodMenuList.first().restaurantId
            firebaseAuth.currentUser?.let { user ->
                when (val data = orderRepository.orderMenu(
                    user.uid,
                    restaurantId,
                    foodMenuList
                )) {
                    is DefaultOrderRepository.Result.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        setState(OrderMenuState.Order)
                    }
                    is DefaultOrderRepository.Result.Error -> {
                        setState(OrderMenuState.Error(
                            R.string.request_error, data.e
                        ))
                    }
                }
            } ?: kotlin.run {
                setState(OrderMenuState.Error(
                    R.string.user_id_not_found, IllegalAccessException()
                ))
            }
        }
    }

    fun clearOrderMenu() = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(model: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuInBasket(model.foodId)
        fetchData()
    }
}