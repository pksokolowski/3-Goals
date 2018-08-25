package pksokolowski.github.com.threegoals.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection
import pksokolowski.github.com.threegoals.notifications.NotificationsManager
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import javax.inject.Inject

class AlarmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var editionsRepo: EditionsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        AndroidInjection.inject(this, context)


        // first show notification if needed:
        NotificationsManager.showNotificationIfNeeded(context, editionsRepo)

        // if edition ended, cancel the alarms
        if (editionsRepo.getCurrentEdition() == null) {
            AlarmsManager.cancelAlarms(context)
        }
    }
}