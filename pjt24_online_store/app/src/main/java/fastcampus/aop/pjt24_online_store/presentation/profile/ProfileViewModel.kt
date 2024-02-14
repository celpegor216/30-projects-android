package fastcampus.aop.pjt24_online_store.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import fastcampus.aop.pjt24_online_store.data.preference.PreferenceManager
import fastcampus.aop.pjt24_online_store.domain.DeleteOrderedProductListUseCase
import fastcampus.aop.pjt24_online_store.domain.GetOrderedProductListUseCase
import fastcampus.aop.pjt24_online_store.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager,
    private val getOrderedProductListUseCase: GetOrderedProductListUseCase,
    private val deleteOrderedProductListUseCase: DeleteOrderedProductListUseCase
) : BaseViewModel() {

    private var _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)
    val profileStateLiveData: LiveData<ProfileState> = _profileStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProfileState.Loading)
        preferenceManager.getIdToken()?.let {
            setState(ProfileState.Login(it))
        } ?: kotlin.run {
            setState(ProfileState.Success.NotRegistered)
        }
    }

    private fun setState(state: ProfileState) {
        _profileStateLiveData.postValue(state)
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            setState(
                ProfileState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl,
                    getOrderedProductListUseCase()
                )
            )
        } ?: kotlin.run {
            setState(ProfileState.Success.NotRegistered)
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.removeIdToken()
        }
        deleteOrderedProductListUseCase()
        fetchData()
    }
}