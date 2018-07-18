package pksokolowski.github.com.threegoals.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import pksokolowski.github.com.threegoals.R

object NotificationsManager {
    val CHANNEL_ID_USER_REPORT_REQUEST = "user_report_request"
    val NOTIFICATION_ID_USER_REPORT_REQUEST = 0
    val ACTION_OPEN_REPORTER = "com.github.pksokolowski.threegoals.action.open_reporter"

    public fun showNotification(context: Context) {

        val B = NotificationCompat.Builder(context, CHANNEL_ID_USER_REPORT_REQUEST)
                .setContentText(context.getString(R.string.notification_open_reporter_content_text))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_timeline_white_24dp)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(getOpenReporterPendingIntent(context))

        val notif = B.build()
        val NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // to 0 to ID, pozwoli mi potem manipulować tym notification, w szczególności je usunąc lub zastąpić
        NotificationManager.notify(NOTIFICATION_ID_USER_REPORT_REQUEST, notif)
    }

    public fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                ?: return
        manager.cancel(NOTIFICATION_ID_USER_REPORT_REQUEST)
    }

    private fun getOpenReporterPendingIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(ACTION_OPEN_REPORTER), PendingIntent.FLAG_CANCEL_CURRENT)
    }

    public fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_user_report_request_title)
            val description = context.getString(R.string.notification_channel_user_report_request_description)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_USER_REPORT_REQUEST, name, importance)
            channel.description = description

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}