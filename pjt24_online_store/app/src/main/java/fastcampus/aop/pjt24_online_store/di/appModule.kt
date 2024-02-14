package fastcampus.aop.pjt24_online_store.di

import fastcampus.aop.pjt24_online_store.data.db.provideDB
import fastcampus.aop.pjt24_online_store.data.db.provideProductDao
import fastcampus.aop.pjt24_online_store.data.network.buildOkHttpClient
import fastcampus.aop.pjt24_online_store.data.network.provideGsonConverterFactory
import fastcampus.aop.pjt24_online_store.data.network.provideProductApiService
import fastcampus.aop.pjt24_online_store.data.network.provideProductRetrofit
import fastcampus.aop.pjt24_online_store.data.preference.PreferenceManager
import fastcampus.aop.pjt24_online_store.data.repository.DefaultProductRepository
import fastcampus.aop.pjt24_online_store.data.repository.ProductRepository
import fastcampus.aop.pjt24_online_store.domain.DeleteOrderedProductListUseCase
import fastcampus.aop.pjt24_online_store.domain.GetOrderedProductListUseCase
import fastcampus.aop.pjt24_online_store.domain.GetProductItemUseCase
import fastcampus.aop.pjt24_online_store.domain.GetProductListUseCase
import fastcampus.aop.pjt24_online_store.domain.OrderProductItemUseCase
import fastcampus.aop.pjt24_online_store.presentation.detail.ProductDetailViewModel
import fastcampus.aop.pjt24_online_store.presentation.list.ProductListViewModel
import fastcampus.aop.pjt24_online_store.presentation.main.MainViewModel
import fastcampus.aop.pjt24_online_store.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get()) }

    // Coroutine Dispatchers
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Repositories
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }

    // Retrofit
    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

    // UseCases
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { DeleteOrderedProductListUseCase(get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideProductDao(get()) }

    // SharedPreferences
    single { PreferenceManager(androidContext()) }
}