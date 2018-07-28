package pksokolowski.github.com.threegoals.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pksokolowski.github.com.threegoals.EditionsManager
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

class AlarmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // check if there is an edition going on. remove alarm if not
        if(context==null) return

        // first show notification if needed:
        NotificationsManager.showNotificationIfNeeded(context)

        // if edition ended, cancel the alarms
        if( EditionsManager.getCurrentEdition(context) == null){
            AlarmsManager.cancelAlarms(context)
        }
    }
}