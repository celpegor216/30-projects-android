package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review.RestaurantReviewRepository
import fastcampus.aop.pjt30_food_delivery.model.restaurant.review.RestaurantReviewModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    private val _reviewStateLiveData =
        MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)
    val reviewStateLiveData: LiveData<RestaurantReviewState> = _reviewStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(RestaurantReviewState.Loading)

        val reviews = restaurantReviewRepository.getReviews(restaurantTitle)

        setState(
            RestaurantReviewState.Success(
                reviews.map {
                    RestaurantReviewModel(
                        id = it.id,
                        title = it.title,
                        description =  it.description,
                        grade = it.grade,
                        thumbnailImageUri = it.images?.first()
                    )
                }
            )
        )
    }

    private fun setState(state: RestaurantReviewState) {
        _reviewStateLiveData.postValue(state)
    }

}