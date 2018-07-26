package pksokolowski.github.com.threegoals

import pksokolowski.github.com.threegoals.models.DaysData
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class TimeHelper {
    companion object {

        public val yearInMillis = 31536000000L
        public val dayInMillis = 86400000L

        public fun getYear(timeStamp: Long): String {
            val formatter = SimpleDateFormat("yyyy")
            return formatter.format(Date(timeStamp))
        }

        public fun getYearLast2Digits(timeStamp: Long): String {
            val formatter = SimpleDateFormat("yy")
            return formatter.format(Date(timeStamp))
        }

        fun getDayOfWeekShortName(timeStamp: Long): String {
            val c = Calendar.getInstance()
            c.timeInMillis = timeStamp
            return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        }

        fun getDayOfWeekShortNameByDaysNumber(dayOfWeek: Int): String{
            val c = Calendar.getInstance()
            c.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            return getDayOfWeekShortName(c.timeInMillis)
        }

        fun getDate(timeStamp: Long): String {
            val formatter = SimpleDateFormat("dd.MM.yy")
            return formatter.format(Date(timeStamp))
        }

        fun get0HourTimeOfAGivenDay(any_moment_within_the_day: Long): Long {
            val c = Calendar.getInstance()
            c.timeInMillis = any_moment_within_the_day
            // zero every field that's supposed to be zero
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)

            return c.timeInMillis
        }

        fun now() = Calendar.getInstance().timeInMillis
        fun yesterday0Hour() = get0HourTimeOfAGivenDay(now() - dayInMillis)

        fun dayOfWeekOf(timeStamp: Long): Int {
            val c = Calendar.getInstance()
            c.timeInMillis = timeStamp

            return c.get(Calendar.DAY_OF_WEEK)
        }
    }
}