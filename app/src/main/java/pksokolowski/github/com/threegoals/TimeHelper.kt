package pksokolowski.github.com.threegoals

import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {
    companion object {

        val yearInMillis = 31536000000L
        val dayInMillis = 86400000L

        fun getYear(timeStamp: Long): String {
            val formatter = SimpleDateFormat("yyyy")
            return formatter.format(Date(timeStamp))
        }

        fun getYearLast2Digits(timeStamp: Long): String {
            val formatter = SimpleDateFormat("yy")
            return formatter.format(Date(timeStamp))
        }

        fun getDayOfWeekShortName(timeStamp: Long): String {
            val c = Calendar.getInstance()
            c.timeInMillis = timeStamp
            return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        }

        fun getDayOfWeekShortNameByDaysNumber(dayOfWeek: Int): String {
            val c = Calendar.getInstance()
            val currentCultureBased = convertDayOfWeekBetweenCultures(dayOfWeek, ConversionDiraction.TO_SUNDAY_BASED)
            c.set(Calendar.DAY_OF_WEEK, currentCultureBased)
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
            val sundayBased = c.get(Calendar.DAY_OF_WEEK)
            return convertDayOfWeekBetweenCultures(sundayBased, ConversionDiraction.TO_CURRENT_CULTURE)
        }

        enum class ConversionDiraction(val multiplier: Int) {
            TO_CURRENT_CULTURE(-1),
            TO_SUNDAY_BASED(1)
        }

        fun convertDayOfWeekBetweenCultures(dayOfWeek: Int, direction: ConversionDiraction): Int {
            // only days of week of 1 through 7 are accepted
            require(dayOfWeek in 1..7)

            // ensure use of valid directional
            // convert to zero based, in order to work with a 0-6 instead 1-7 range
            // this allows use of more convenient modulo transformation instead of 2 special cases.
            val zeroBasedDayOfWeek = dayOfWeek - 1

            // this gives us the shift in days between current culture and the default, sunday based
            // constant values like Calendar.SUNDAY etc
            val culturalDifference = Calendar.getInstance().firstDayOfWeek - 1

            // shift dayOfWeek by the difference times direction
            var zeroBasedShiftedDay = (zeroBasedDayOfWeek + (direction.multiplier * culturalDifference)) % 7
            // modulo operator is not floorMod, so negative numbers can happen, lets remove them
            if (zeroBasedShiftedDay < 0) zeroBasedShiftedDay += 7

            // return after shifting back by 1 ahead, so the first dayOfWeek of week is number 1 and the
            // last is number 7 again.
            return zeroBasedShiftedDay + 1
        }
    }
}