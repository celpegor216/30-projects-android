package fastcampus.aop.pjt28_delivery_info.di

import android.app.Activity
import fastcampus.aop.pjt28_delivery_info.BuildConfig
import fastcampus.aop.pjt28_delivery_info.data.api.SweetTrackerApi
import fastcampus.aop.pjt28_delivery_info.data.api.Url
import fastcampus.aop.pjt28_delivery_info.data.db.AppDatabase
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.data.preference.PreferenceManager
import fastcampus.aop.pjt28_delivery_info.data.preference.SharedPreferenceManager
import fastcampus.aop.pjt28_delivery_info.data.repository.ShippingCompanyRepository
import fastcampus.aop.pjt28_delivery_info.data.repository.ShippingCompanyRepositoryImpl
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepository
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepositoryImpl
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepositoryStub
import fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem.AddTrackingItemContract
import fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem.AddTrackingItemFragment
import fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem.AddTrackingItemPresenter
import fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory.TrackingHistoryContract
import fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory.TrackingHistoryFragment
import fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory.TrackingHistoryPresenter
import fastcampus.aop.pjt28_delivery_info.presentation.trackingitems.TrackingItemsContract
import fastcampus.aop.pjt28_delivery_info.presentation.trackingitems.TrackingItemsFragment
import fastcampus.aop.pjt28_delivery_info.presentation.trackingitems.TrackingItemsPresenter
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {
    // Dispatchers
    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Repository
    single<TrackingItemRepository> { TrackingItemRepositoryStub() }
//    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource()!!, get()) }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemContract.Presenter> { AddTrackingItemPresenter(getSource()!!, get(), get()) }
    }
    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> {
            (trackingItem: TrackingItem, trackingInformation: TrackingInformation) ->
            TrackingHistoryPresenter(getSource()!!, get(), trackingItem, trackingInformation) }
    }
}