package fastcampus.aop.pjt24_online_store.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt24_online_store.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MainViewModel: BaseViewModel() {

    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData: LiveData<MainState> = _mainStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {  }

    fun refreshOrderList() = viewModelScope.launch {
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}