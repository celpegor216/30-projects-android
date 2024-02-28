package fastcampus.aop.pjt30_food_delivery.di

import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.map.DefaultMapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.map.MapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.DefaultRestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.RestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.DefaultUserRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.screen.main.home.HomeViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.my.MyViewModel
import fastcampus.aop.pjt30_food_delivery.screen.mylocation.MyLocationViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.DefaultResourcesProvider
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // Coroutine
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // ResourcesProvider
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    // Retrofit
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single(named("map")) { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // ViewModel
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLngEntity, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(
            mapSearchInfoEntity, get(), get()
        )
    }
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(restaurantEntity, get(), get())
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(
            restaurantId,
            restaurantFoodList
        )
    }
    viewModel { RestaurantReviewListViewModel() }

    // Repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
}