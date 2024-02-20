package fastcampus.aop.pjt27_subway_info.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fastcampus.aop.pjt27_subway_info.BuildConfig
import fastcampus.aop.pjt27_subway_info.data.api.StationApi
import fastcampus.aop.pjt27_subway_info.data.api.StationArrivalsApi
import fastcampus.aop.pjt27_subway_info.data.api.StationStorageApi
import fastcampus.aop.pjt27_subway_info.data.api.Url
import fastcampus.aop.pjt27_subway_info.data.db.AppDatabase
import fastcampus.aop.pjt27_subway_info.data.preference.PreferenceManager
import fastcampus.aop.pjt27_subway_info.data.preference.SharedPreferenceManager
import fastcampus.aop.pjt27_subway_info.data.repository.StationRepository
import fastcampus.aop.pjt27_subway_info.data.repository.StationRepositoryImpl
import fastcampus.aop.pjt27_subway_info.presentation.stationarrivals.StationArrivalsContract
import fastcampus.aop.pjt27_subway_info.presentation.stationarrivals.StationArrivalsFragment
import fastcampus.aop.pjt27_subway_info.presentation.stationarrivals.StationArrivalsPresenter
import fastcampus.aop.pjt27_subway_info.presentation.stations.StationsContract
import fastcampus.aop.pjt27_subway_info.presentation.stations.StationsFragment
import fastcampus.aop.pjt27_subway_info.presentation.stations.StationsPresenter
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// 의존성 정의
val appModule = module {

    // Dispatchers
    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Api
    single<StationApi> { StationStorageApi(Firebase.storage) }
    single { OkHttpClient().newBuilder()
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
    single<StationArrivalsApi> { Retrofit.Builder()
        .baseUrl(Url.SEOUL_DATA_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(get())
        .build()
        .create()
    }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get(), get()) }

    // Presentation
    // scope 내에 정의된 의존성들은 공유 및 재사용 가능
    // single(global), factory(lifecycle이 짧고 공유 불가)도 scope의 일종
    // 메모리 관리를 효율적으로 수행할 수 있음
    // StationsFragment는 ScopeFragment()를 구현해야 함
    // StationsFragment가 종료되면 내부의 의존성들을 사용할 수 없음
    scope<StationsFragment> {
        scoped<StationsContract.Presenter> { StationsPresenter(getSource()!!, get()) }
    }
    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource()!!, get(), get()) }
    }
}