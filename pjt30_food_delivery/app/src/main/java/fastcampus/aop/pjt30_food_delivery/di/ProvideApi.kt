package fastcampus.aop.pjt30_food_delivery.di

import fastcampus.aop.pjt30_food_delivery.BuildConfig
import fastcampus.aop.pjt30_food_delivery.data.network.FoodApiService
import fastcampus.aop.pjt30_food_delivery.data.network.MapApiService
import fastcampus.aop.pjt30_food_delivery.data.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

fun provideGsonConvertFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        } else {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

fun provideMapRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.TMAP_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideMapApiService(retrofit: Retrofit): MapApiService {
    return retrofit.create(MapApiService::class.java)
}

fun provideFoodRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.FOOD_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideFoodApiService(retrofit: Retrofit): FoodApiService {
    return retrofit.create(FoodApiService::class.java)
}