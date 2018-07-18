package pksokolowski.github.com.threegoals.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import java.util.*

object AlarmsManager {

    val ACTION_REQUEST_USER_REPORT = "com.github.pksokolowski.threegoals.action.request_user_report"

    public fun setupAlarms(context: Context) {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = getRequestUserReportPendingIntent(context)
        if (Build.VERSION.SDK_INT >= 23) {
            am.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            am.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    public fun cancelAlarms(context: Context){
        val pending = getRequestUserReportPendingIntent(context)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pending)
    }

    private fun getRequestUserReportPendingIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(ACTION_REQUEST_USER_REPORT), PendingIntent.FLAG_CANCEL_CURRENT)
    }
}