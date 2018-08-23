package pksokolowski.github.com.threegoals.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pksokolowski.github.com.threegoals.EditionsManager
import pksokolowski.github.com.threegoals.TimeHelper
import pksokolowski.github.com.threegoals.notifications.NotificationsManager.ACTION_OPEN_REPORTER
import pksokolowski.github.com.threegoals.reporter.ReporterActivity

class NotificationsClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == ACTION_OPEN_REPORTER) {
            val latestEdition = EditionsManager.getLatestEdition(context)
            if(latestEdition == null) { onFailure(context); return }
            val dayOfEdition = latestEdition.dayNumOf(TimeHelper.yesterday0Hour())
            if(dayOfEdition < 0) { onFailure(context); return }

            context.startActivity(ReporterActivity.newIntent(context, latestEdition.id, dayOfEdition))
        }
    }

    private fun onFailure(context: Context){
        NotificationsManager.cancelNotification(context)
    }
}