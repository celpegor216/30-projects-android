package fastcampus.aop.pjt30_food_delivery.screen.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel

class HomeViewModel: BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)
    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData

    private fun setState(state: HomeState) {
        _homeStateLiveData.postValue(state)
    }
}