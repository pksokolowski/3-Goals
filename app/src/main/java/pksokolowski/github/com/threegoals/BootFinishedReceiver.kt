package pksokolowski.github.com.threegoals

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import pksokolowski.github.com.threegoals.alarms.AlarmsManager
import pksokolowski.github.com.threegoals.notifications.NotificationsManager
import android.content.pm.PackageManager
import android.content.ComponentName
import dagger.android.AndroidInjection
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import javax.inject.Inject


class BootFinishedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var editionsRepo: EditionsRepository

    @Inject
    lateinit var notificationsManager: NotificationsManager

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        AndroidInjection.inject(this, context)

        if (action == ACTION_ANDROID_BOOT_COMPLETED) {
            val currentEdition = editionsRepo.getCurrentEdition()
            if (currentEdition != null) {
                AlarmsManager.setupAlarms(context)
            } else {
                // when there is no ongoing edition, disable the receiver
                // to prevent it from running on every system restart
                setBootFinishedReceiverEnabled(context, false)
            }
            notificationsManager.showNotificationIfNeeded()
        }
    }

    companion object {
        const val ACTION_ANDROID_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"

        fun setBootFinishedReceiverEnabled(context: Context, enabled: Boolean) {
            val newState = if (enabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED

            val receiver = ComponentName(context, BootFinishedReceiver::class.java)
            val pm = context.packageManager

            pm.setComponentEnabledSetting(receiver,
                    newState,
                    PackageManager.DONT_KILL_APP)
        }
    }
}