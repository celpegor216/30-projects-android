package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review

import fastcampus.aop.pjt30_food_delivery.model.restaurant.review.RestaurantReviewModel

sealed class RestaurantReviewState {

    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>
    ): RestaurantReviewState()

}