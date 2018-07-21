package pksokolowski.github.com.threegoals

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import pksokolowski.github.com.threegoals.alarms.AlarmsManager
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.notifications.NotificationsManager


class BootFinishedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == ACTION_ANDROID_BOOT_COMPLETED) {
            val currentEdition = EditionsManager.getCurrentEdition(context)
            if (currentEdition != null) {
                AlarmsManager.setupAlarms(context)
                showNotificationIfNeeded(context, currentEdition)
            }
        }
    }

    private fun showNotificationIfNeeded(context: Context, edition: Edition) {
        val db = DbHelper.getInstance(context)

        // check if yesterday is eligible for getting a report
        val yesterday0Hour = TimeHelper.yesterday0Hour()
        val yesterdayNum = edition.dayNumOf(yesterday0Hour)
        if (yesterdayNum < 0) return

        // check if there is already a report for the day
        val hasReports = db.isThereReportForDay(yesterdayNum, edition)
        if (hasReports) return

        NotificationsManager.showNotification(context)
    }

    companion object {
        val ACTION_ANDROID_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }
}