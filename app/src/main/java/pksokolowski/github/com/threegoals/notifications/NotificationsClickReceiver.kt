package pksokolowski.github.com.threegoals.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pksokolowski.github.com.threegoals.notifications.NotificationsManager.ACTION_OPEN_REPORTER

class NotificationsClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == ACTION_OPEN_REPORTER) {

        }
    }
}