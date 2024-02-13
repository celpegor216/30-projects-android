package fastcampus.aop.pjt23_todo

import android.app.Application
import fastcampus.aop.pjt23_todo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Pjt23TodoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Koin Trigger
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Pjt23TodoApplication)
            modules(appModule)
        }
    }
}