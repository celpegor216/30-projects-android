package fastcampus.aop.pjt30_food_delivery.di

import com.google.firebase.auth.FirebaseAuth
import fastcampus.aop.pjt30_food_delivery.data.TestOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.TestRestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.TestRestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.RestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.order.OrderMenuListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// 내부 모듈에서만 사용
internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLngEntity, get())
    }
    viewModel { (firebaseAuth: FirebaseAuth) ->
        OrderMenuListViewModel(get(), get(), firebaseAuth)
    }

    single<RestaurantRepository> { TestRestaurantRepository() }
    single<RestaurantFoodRepository> { TestRestaurantFoodRepository() }
    single<OrderRepository> { TestOrderRepository() }
}