package fastcampus.aop.pjt27_subway_info

import android.app.Application
import fastcampus.aop.pjt27_subway_info.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Pjt27Application: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) {
                    Level.DEBUG
                } else {
                    Level.NONE
                }
            )
            androidContext(this@Pjt27Application)
            modules(appModule)
        }
    }
}