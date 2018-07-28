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
            }
            NotificationsManager.showNotificationIfNeeded(context)
        }
    }

    companion object {
        const val ACTION_ANDROID_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }
}