package fastcampus.aop.pjt29_movie_review.di

import android.app.Activity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fastcampus.aop.pjt29_movie_review.data.api.MovieApi
import fastcampus.aop.pjt29_movie_review.data.api.MovieFirestoreApi
import fastcampus.aop.pjt29_movie_review.data.api.ReviewApi
import fastcampus.aop.pjt29_movie_review.data.api.ReviewFirestoreApi
import fastcampus.aop.pjt29_movie_review.data.api.UserApi
import fastcampus.aop.pjt29_movie_review.data.api.UserFirestoreApi
import fastcampus.aop.pjt29_movie_review.data.preference.PreferenceManager
import fastcampus.aop.pjt29_movie_review.data.preference.SharedPreferenceManager
import fastcampus.aop.pjt29_movie_review.data.repository.MovieRepository
import fastcampus.aop.pjt29_movie_review.data.repository.MovieRepositoryImpl
import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepository
import fastcampus.aop.pjt29_movie_review.data.repository.ReviewRepositoryImpl
import fastcampus.aop.pjt29_movie_review.data.repository.UserRepository
import fastcampus.aop.pjt29_movie_review.data.repository.UserRepositoryImpl
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.usecase.AddReviewUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.DeleteReviewUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetAllMoviesUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetAllReviewsUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetMyReviewedMoviesUseCase
import fastcampus.aop.pjt29_movie_review.domain.usecase.GetRandomFeaturedMovieUseCase
import fastcampus.aop.pjt29_movie_review.presentation.home.HomeContract
import fastcampus.aop.pjt29_movie_review.presentation.home.HomeFragment
import fastcampus.aop.pjt29_movie_review.presentation.home.HomePresenter
import fastcampus.aop.pjt29_movie_review.presentation.mypage.MyPageContract
import fastcampus.aop.pjt29_movie_review.presentation.mypage.MyPageFragment
import fastcampus.aop.pjt29_movie_review.presentation.mypage.MyPagePresenter
import fastcampus.aop.pjt29_movie_review.presentation.reviews.ReviewsContract
import fastcampus.aop.pjt29_movie_review.presentation.reviews.ReviewsFragment
import fastcampus.aop.pjt29_movie_review.presentation.reviews.ReviewsPresenter
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // Dispatcher
    single { Dispatchers.IO }
}

val dataModule = module {
    single { Firebase.firestore }

    // Api
    single<MovieApi> { MovieFirestoreApi(get()) }
    single<ReviewApi> { ReviewFirestoreApi(get()) }
    single<UserApi> { UserFirestoreApi(get()) }

    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }

    // SharedPreference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }
}

val domainModule = module {
    // UseCase
    factory { GetAllMoviesUseCase(get()) }
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllReviewsUseCase(get(), get()) }
    factory { GetMyReviewedMoviesUseCase(get(), get(), get()) }
    factory { AddReviewUseCase(get(), get()) }
    factory { DeleteReviewUseCase(get()) }
}

val presenterModule = module {
    // Fragment
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(get(), get(), get()) }
    }
    scope<ReviewsFragment> {
        scoped<ReviewsContract.Presenter> { (movie: Movie) -> ReviewsPresenter(getSource()!!, get(), get(), get(), get()) }
    }
    scope<MyPageFragment> {
        scoped<MyPageContract.Presenter> { MyPagePresenter(get(), get()) }
    }
}