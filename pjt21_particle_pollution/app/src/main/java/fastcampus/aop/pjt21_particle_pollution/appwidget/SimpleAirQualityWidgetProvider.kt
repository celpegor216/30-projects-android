package fastcampus.aop.pjt21_particle_pollution.appwidget

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import fastcampus.aop.pjt21_particle_pollution.R
import fastcampus.aop.pjt21_particle_pollution.data.Repository
import fastcampus.aop.pjt21_particle_pollution.data.models.airquality.Grade
import kotlinx.coroutines.launch

class SimpleAirQualityWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        ContextCompat.startForegroundService(
            context!!,
            Intent(context, UpdateWidgetService::class.java)
        )
    }

    // foreground service로 위치 정보를 얻고 데이터 갱신
    class UpdateWidgetService : LifecycleService() {
        override fun onCreate() {
            super.onCreate()

            createNotificationChannelIfNeeded()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    createNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } else {
                startForeground(
                    NOTIFICATION_ID,
                    createNotification()
                )
            }
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val updateViews = RemoteViews(packageName, R.layout.widget_simple).apply {
                    setTextViewText(R.id.resultTextView, "권한 없음")
                    setViewVisibility(R.id.labelTextView, View.GONE)
                    setViewVisibility(R.id.gradeLabelTextView, View.GONE)
                }

                updateWidget(updateViews)
                stopSelf()

                return super.onStartCommand(intent, flags, startId)
            }

            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener { location ->
                    lifecycleScope.launch {
                        try {
                            val nearbyMonitoringStation = Repository.getNearbyMonitoringStation(
                                location.latitude,
                                location.longitude
                            )

                            val measuredValue =
                                Repository.getLatestAirQualityData(nearbyMonitoringStation!!.stationName!!)

                            val updateViews =
                                RemoteViews(packageName, R.layout.widget_simple).apply {
                                    setViewVisibility(R.id.labelTextView, View.VISIBLE)
                                    setViewVisibility(R.id.gradeLabelTextView, View.VISIBLE)

                                    val currentGrade = (measuredValue?.khaiGrade ?: Grade.UNKNOWN)

                                    setTextViewText(R.id.resultTextView, currentGrade.emoji)
                                    setTextViewText(R.id.gradeLabelTextView, currentGrade.label)
                                }

                            updateWidget(updateViews)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            stopSelf()
                        }
                    }
                }

            return super.onStartCommand(intent, flags, startId)
        }

        override fun onDestroy() {
            super.onDestroy()

            stopForeground(true)
        }

        // Android 12 이상부터는 ForegroundServiceStartNotAllowedException를 피하기 위해
        // IMPORTANCE_HIGH로 지정해야 함
        // https://developer.android.com/about/versions/12/foreground-services?hl=ko#cases-fgs-background-starts-allowed
        private fun createNotificationChannelIfNeeded() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(
                    NotificationChannel(
                        WIDGET_REFRESH_CHANNEL_ID,
                        "위젯 갱신 채널",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
        }

        private fun createNotification() = NotificationCompat.Builder(this)
            .setChannelId(WIDGET_REFRESH_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_refresh_24)
            .build()

        private fun updateWidget(updateViews: RemoteViews) {
            val widgetProvider = ComponentName(this, SimpleAirQualityWidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider, updateViews)
        }
    }

    companion object {
        private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH_CHANNEL_ID"
        private const val NOTIFICATION_ID = 200
    }
}