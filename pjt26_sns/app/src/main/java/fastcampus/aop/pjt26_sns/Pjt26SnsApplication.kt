package fastcampus.aop.pjt26_sns

import android.app.Application
import android.content.Context

class Pjt26SnsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
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