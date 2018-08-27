package pksokolowski.github.com.threegoals.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection
import pksokolowski.github.com.threegoals.utils.TimeHelper
import pksokolowski.github.com.threegoals.notifications.NotificationHelper.Companion.ACTION_OPEN_REPORTER
import pksokolowski.github.com.threegoals.reporter.ReporterActivity
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import javax.inject.Inject

class NotificationsClickReceiver : BroadcastReceiver() {

    @Inject
    lateinit var editionsRepo: EditionsRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        AndroidInjection.inject(this, context)

        if (action == ACTION_OPEN_REPORTER) {
            val latestEdition = editionsRepo.getLatestEdition()
            val dayOfEdition = latestEdition.dayNumOf(TimeHelper.yesterday0Hour())
            if(dayOfEdition < 0) { onFailure(); return }

            context.startActivity(ReporterActivity.newIntent(context, latestEdition.id, dayOfEdition))
        }
    }

    private fun onFailure(){
        notificationHelper.cancelNotification()
    }
}