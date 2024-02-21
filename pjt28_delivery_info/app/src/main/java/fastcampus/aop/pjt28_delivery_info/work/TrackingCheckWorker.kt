package fastcampus.aop.pjt28_delivery_info.work

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fastcampus.aop.pjt28_delivery_info.R
import fastcampus.aop.pjt28_delivery_info.data.entity.Level
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepository
import fastcampus.aop.pjt28_delivery_info.presentation.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TrackingCheckWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val trackingItemRepository: TrackingItemRepository by inject()
    private val dispatcher: CoroutineDispatcher by inject()

    override suspend fun doWork(): Result = withContext(dispatcher) {
        try {
            val startedTrackingItems = trackingItemRepository.getTrackingItemsInformation()
                .filter { it.second.level == Level.START }

            if (startedTrackingItems.isNotEmpty()) {
                createNotificationChannelIfNeeded()

                val representativeItem = startedTrackingItems.first()

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@withContext Result.failure()
                }
                NotificationManagerCompat.from(context)
                    .notify(
                        NOTIFICATION_ID,
                        createNotification(
                            "${representativeItem.second.itemName}(${representativeItem.first.company.name}\n외 ${startedTrackingItems.size - 1}건의 택배가 배송 출발하였습니다."
                        )
                    )
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(message: String?): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_local_shipping_24)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1000
        private const val CHANNEL_ID = "TrackingCheck_ID"
        private const val CHANNEL_NAME = "TrackingCheck_NAME"
        private const val CHANNEL_DESCRIPTION = "TrackingCheck_DESCRIPTION"
    }
}