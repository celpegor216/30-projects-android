package fastcampus.aop.pjt28_delivery_info

import android.app.Application
import fastcampus.aop.pjt28_delivery_info.di.appModule
import fastcampus.aop.pjt28_delivery_info.di.workManagerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Pjt28Application: Application() {

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
            androidContext(this@Pjt28Application)
            workManagerFactory()
            modules(appModule, workManagerModule)
        }
    }
}