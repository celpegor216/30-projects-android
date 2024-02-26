package fastcampus.aop.pjt30_food_delivery.di

import fastcampus.aop.pjt30_food_delivery.data.repository.DefaultRestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.RestaurantRepository
import fastcampus.aop.pjt30_food_delivery.screen.main.home.HomeViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.my.MyViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.DefaultResourcesProvider
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
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
    single { provideRetrofit(get(), get()) }

    // ViewModel
    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }

    // Repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
}