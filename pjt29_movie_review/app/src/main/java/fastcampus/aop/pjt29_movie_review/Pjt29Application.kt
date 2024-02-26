package fastcampus.aop.pjt29_movie_review

import android.app.Application
import fastcampus.aop.pjt29_movie_review.di.appModule
import fastcampus.aop.pjt29_movie_review.di.dataModule
import fastcampus.aop.pjt29_movie_review.di.domainModule
import fastcampus.aop.pjt29_movie_review.di.presenterModule
import fastcampus.aop.pjt29_movie_review.utility.MovieDataGenerator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Pjt29Application: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Pjt29Application)
            androidLogger(
                if (BuildConfig.DEBUG)
                    Level.DEBUG
                else
                    Level.NONE
            )
            modules(appModule + dataModule + domainModule + presenterModule)
        }

        // Firestore에 데이터를 추가하기 위한 용도
        // 데이터가 중복으로 추가되지 않도록 1회 실행 후 주석 처리
        // MovieDataGenerator().generate()
    }
}