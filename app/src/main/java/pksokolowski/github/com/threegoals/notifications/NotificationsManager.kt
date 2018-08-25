package pksokolowski.github.com.threegoals.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.TimeHelper
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsManager @Inject constructor(private val context: Application, private val editionsRepo: EditionsRepository, private val reportsRepo: ReportsRepository){
    val CHANNEL_ID_USER_REPORT_REQUEST = "user_report_request"
    val NOTIFICATION_ID_USER_REPORT_REQUEST = 0
    companion object {
        const val ACTION_OPEN_REPORTER = "com.github.pksokolowski.threegoals.action.open_reporter"
    }

    fun showNotification() {
        val B = NotificationCompat.Builder(context, CHANNEL_ID_USER_REPORT_REQUEST)
                .setContentText(context.getString(R.string.notification_open_reporter_content_text))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_timeline_white_24dp)
                .setOngoing(true)
                .setContentIntent(getOpenReporterPendingIntent(context))

        val notif = B.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // to 0 to id, pozwoli mi potem manipulować tym notification, w szczególności je usunąc lub zastąpić
        notificationManager.notify(NOTIFICATION_ID_USER_REPORT_REQUEST, notif)
    }

    fun showNotificationIfNeeded() {
        val edition = editionsRepo.getLatestEdition()

        // check if yesterday is eligible for getting a report within the last edition
        val yesterday0Hour = TimeHelper.yesterday0Hour()
        val yesterdayNum = edition.dayNumOf(yesterday0Hour)
        if (yesterdayNum < 0) return

        // check if there is already a report for the day
        val hasReports = reportsRepo.getReportsForDay(edition, yesterdayNum).size > 0

        if (hasReports) return

        showNotification()
    }


    fun cancelNotification() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID_USER_REPORT_REQUEST)
    }

    private fun getOpenReporterPendingIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(ACTION_OPEN_REPORTER), PendingIntent.FLAG_CANCEL_CURRENT)
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_user_report_request_title)
            val description = context.getString(R.string.notification_channel_user_report_request_description)

            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID_USER_REPORT_REQUEST, name, importance)
            channel.description = description

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}