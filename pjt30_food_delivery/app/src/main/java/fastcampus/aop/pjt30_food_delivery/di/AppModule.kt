package fastcampus.aop.pjt30_food_delivery.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.preference.AppPreferenceManager
import fastcampus.aop.pjt30_food_delivery.data.repository.gallery.DefaultGalleryPhotoRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.gallery.GalleryPhotoRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.map.DefaultMapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.map.MapRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.DefaultOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.DefaultRestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.RestaurantRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.review.RestaurantReviewRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.DefaultUserRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.user.UserRepository
import fastcampus.aop.pjt30_food_delivery.screen.main.home.HomeViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.like.RestaurantLikeListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.my.MyViewModel
import fastcampus.aop.pjt30_food_delivery.screen.mylocation.MyLocationViewModel
import fastcampus.aop.pjt30_food_delivery.screen.order.OrderMenuListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.review.gallery.GalleryViewModel
import fastcampus.aop.pjt30_food_delivery.util.event.MenuChangeEventBus
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

    // Retrofit
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single(named("map")) { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // ViewModel
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get(), get()) }
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
            restaurantFoodList,
            get()
        )
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }
    viewModel { GalleryViewModel(get()) }

    // Repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }
    single<GalleryPhotoRepository> { DefaultGalleryPhotoRepository(androidApplication(), get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    // SharedPreferences
    single { AppPreferenceManager(androidApplication()) }

    // ResourcesProvider
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    // Event
    single { MenuChangeEventBus() }

    // Firebase Auth
    single { FirebaseAuth.getInstance() }

    // Firestore
    single { Firebase.firestore }

    // Firebase Storage
    single { Firebase.storage }
}