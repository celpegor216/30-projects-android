package fastcampus.aop.pjt30_food_delivery.screen.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.map.MapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)
    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData

    private val _foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()
    val foodMenuBasketLiveData: LiveData<List<RestaurantFoodEntity>> = _foodMenuBasketLiveData

    private fun setState(state: HomeState) {
        _homeStateLiveData.postValue(state)
    }

    private fun setFoodMenuBasket(foodMenu: List<RestaurantFoodEntity>) {
        _foodMenuBasketLiveData.postValue(foodMenu)
    }

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        setState(HomeState.Loading)

        // 저장된 사용자의 위치와 현재 위치가 동일한지 확인
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            setState(HomeState.Success(
                mapSearchInfo = info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            ))
        } ?: kotlin.run {
            setState(HomeState.Error(
                R.string.can_not_load_address_info
            ))
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        return when (val data = homeStateLiveData.value) {
            is HomeState.Success -> data.mapSearchInfo
            else -> null
        }
    }

    fun checkMyBasket() = viewModelScope.launch {
        setFoodMenuBasket(restaurantFoodRepository.getFoodMenuListInBasket())
    }

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }
}