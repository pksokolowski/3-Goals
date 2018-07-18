package pksokolowski.github.com.threegoals

import android.app.AlarmManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import pksokolowski.github.com.threegoals.alarms.AlarmsManager


class BootFinishedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == ACTION_ANDROID_BOOT_COMPLETED) {
            AlarmsManager.setupAlarms(context)
        }
    }

    companion object {
        val ACTION_ANDROID_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }
}