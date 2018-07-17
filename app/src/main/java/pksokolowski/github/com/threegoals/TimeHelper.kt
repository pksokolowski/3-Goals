package pksokolowski.github.com.threegoals

import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {
    companion object {

        public val yearInMillis = 31536000000
        public val dayInMillis = 86400000

        public fun getYear(timeStamp: Long) : String{
            val formatter = SimpleDateFormat("yyyy")
            return formatter.format(Date(timeStamp))
        }

        public fun getYearLast2Digits(timeStamp: Long) : String{
            val formatter = SimpleDateFormat("yy")
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
    }
}