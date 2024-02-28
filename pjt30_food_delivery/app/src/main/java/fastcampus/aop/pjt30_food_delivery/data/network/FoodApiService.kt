package fastcampus.aop.pjt30_food_delivery.data.network

import fastcampus.aop.pjt30_food_delivery.data.response.restaurant.RestaurantFoodResponse
import fastcampus.aop.pjt30_food_delivery.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {

    @GET(Url.GET_RESTAURANT_FOOD)
    suspend fun getRestaurantFoods(
        @Path("restaurantId") restaurantId: Long
    ): Response<List<RestaurantFoodResponse>>
}