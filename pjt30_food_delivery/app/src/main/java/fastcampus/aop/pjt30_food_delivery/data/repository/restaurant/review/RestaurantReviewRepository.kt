package fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(
        restaurantTitle: String
    ): DefaultRestaurantReviewRepository.Result
}