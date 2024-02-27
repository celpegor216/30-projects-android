package fastcampus.aop.pjt30_food_delivery.screen.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.map.MapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _myLocationStateLiveData =
        MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)
    val myLocationStateLiveData: LiveData<MyLocationState> = _myLocationStateLiveData

    private fun setState(state: MyLocationState) {
        _myLocationStateLiveData.postValue(state)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(MyLocationState.Loading)
        setState(
            MyLocationState.Success(
                mapSearchInfoEntity
            )
        )
    }

    fun changeLocationInfo(locationLatLngEntity: LocationLatLngEntity) = viewModelScope.launch {
        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            setState(
                MyLocationState.Success(
                    info.toSearchInfoEntity(locationLatLngEntity)
                )
            )
        } ?: kotlin.run {
            setState(
                MyLocationState.Error(
                    R.string.can_not_load_address_info
                )
            )
        }
    }

    fun confirmSelectLocation() = viewModelScope.launch {
        when (val data = myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                setState(MyLocationState.Confirm(
                    data.mapSearchInfo
                ))
                userRepository.insertUserLocation(data.mapSearchInfo.locationLatLng)
            }
            else -> Unit
        }
    }
}