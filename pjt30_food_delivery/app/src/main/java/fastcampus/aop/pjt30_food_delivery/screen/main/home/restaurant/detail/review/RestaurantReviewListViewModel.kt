package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.ReviewEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review.DefaultRestaurantReviewRepository
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

        val result = restaurantReviewRepository.getReviews(restaurantTitle)

        when (result) {
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                val reviews = result.data as List<ReviewEntity>

                setState(
                    RestaurantReviewState.Success(
                        reviews.map {
                            RestaurantReviewModel(
                                id = it.hashCode().toLong(),
                                title = it.title,
                                description =  it.content,
                                grade = it.rating,
                                thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                    null
                                } else {
                                    Uri.parse(it.imageUrlList.first())
                                }
                            )
                        }
                    )
                )
            }
            is DefaultRestaurantReviewRepository.Result.Error -> Unit
        }
    }

    private fun setState(state: RestaurantReviewState) {
        _reviewStateLiveData.postValue(state)
    }

}