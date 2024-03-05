package fastcampus.aop.pjt30_food_delivery.screen.main.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity
import fastcampus.aop.pjt30_food_delivery.data.preference.AppPreferenceManager
import fastcampus.aop.pjt30_food_delivery.data.repository.order.DefaultOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.model.order.OrderModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val appPreferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
): BaseViewModel() {

    private val _myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)
    val myStateLiveData: LiveData<MyState> = _myStateLiveData

    private fun setState(state: MyState) {
        _myStateLiveData.postValue(state)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(MyState.Loading)

        appPreferenceManager.getIdToken()?.let {
            setState(MyState.Login(it))
        } ?: run {
            setState(MyState.Success.NotRegistered)
        }
    }

    fun saveToken(idToken: String) = viewModelScope.launch() {
        withContext(ioDispatcher) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch() {
        firebaseUser?.let { user ->
            when (val orderMenuResult = orderRepository.getAllOrderMenu(user.uid)) {
                is DefaultOrderRepository.Result.Success<*> -> {
                    val orderList = orderMenuResult.data as List<OrderEntity>
                    setState(MyState.Success.Registered(
                        username = user.displayName ?: "익명의 사용자",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                restaurantTitle = it.restaurantTitle,
                                foodMenuList = it.foodMenuList
                            )
                        }
                    ))
                }
                is DefaultOrderRepository.Result.Error -> {
                    setState(MyState.Error(R.string.request_error, orderMenuResult.e))
                }
            }
        } ?: run {
            setState(MyState.Success.NotRegistered)
        }
    }

    fun signOut() = viewModelScope.launch() {
        withContext(ioDispatcher) {
            appPreferenceManager.removeIdToken()
        }
        userRepository.deleteAllUserLikedRestaurant()
        fetchData()
    }
}