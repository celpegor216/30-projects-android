package fastcampus.aop.pjt24_online_store.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt24_online_store.domain.GetProductListUseCase
import fastcampus.aop.pjt24_online_store.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductListViewModel(
    private val getProductListUseCase: GetProductListUseCase
): BaseViewModel() {

    // LiveData
    private var _productListStateLiveData = MutableLiveData<ProductListState>(ProductListState.Uninitialized)
    val productListStateLiveData: LiveData<ProductListState> = _productListStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductListState.Loading)
        setState(ProductListState.Success(getProductListUseCase()))
    }

    private fun setState(state: ProductListState) {
        _productListStateLiveData.postValue(state)
    }
}