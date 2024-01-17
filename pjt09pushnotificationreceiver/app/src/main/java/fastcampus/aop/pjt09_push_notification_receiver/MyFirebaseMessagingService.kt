package fastcampus.aop.pjt09_push_notification_receiver

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val type = remoteMessage.data["type"]?.let {
            NotificationType.valueOf(it)
        }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // 직접 인텐트를 다루지 않고 제3자가 인텐트를 처리하도록 전달
        // 동일한 requestCode(여기서는 type.id)를 사용할 경우,
        // 여러 개의 알림을 보내서 여러 개의 pendingIntent를 생성하더라도
        // 마지막의 pendingIntent만 사용됨
        // API 31 이상은 FLAG_IMMUTABLE을 권장
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)    // 클릭 시 자동으로 알림이 닫힘

        when(type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle().bigText("\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29" +
                            "\uD83D\uDE02. \uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0A \uD83D\uDE07 \uD83E\uDD70 \uD83D\uDE0D \uD83E\uDD29")
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}