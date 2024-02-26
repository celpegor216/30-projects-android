package fastcampus.aop.pjt30_food_delivery

import android.app.Application
import android.content.Context
import fastcampus.aop.pjt30_food_delivery.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Pjt30Application : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG)
                    Level.DEBUG
                else
                    Level.NONE
            )
            androidContext(this@Pjt30Application)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}