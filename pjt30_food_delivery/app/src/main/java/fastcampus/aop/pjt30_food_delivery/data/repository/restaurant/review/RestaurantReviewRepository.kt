package fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review

import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(
        restaurantTitle: String
    ): List<RestaurantReviewEntity>
}