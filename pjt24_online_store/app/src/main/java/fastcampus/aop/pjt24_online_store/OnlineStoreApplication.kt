package fastcampus.aop.pjt24_online_store

import android.app.Application
import fastcampus.aop.pjt24_online_store.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class OnlineStoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@OnlineStoreApplication)
            modules(appModule)
        }
    }
}