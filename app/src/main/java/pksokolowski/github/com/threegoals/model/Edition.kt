package pksokolowski.github.com.threegoals.model

import pksokolowski.github.com.threegoals.TimeHelper
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "editions")
data class Edition(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "goalsCount")
        val goalsCount: Int,

        @ColumnInfo(name = "start_day_0_hour_stamp")
        val startDay0HourStamp: Long,

        @ColumnInfo(name = "lengthInDays")
        val lengthInDays: Int

) {
    fun dayNumOf(timestamp: Long): Int {
        if (timestamp < startDay0HourStamp) return -1
        if (timestamp > startDay0HourStamp + (TimeHelper.dayInMillis * lengthInDays)) return -1

        val sinceEditionStart = timestamp - startDay0HourStamp
        return (sinceEditionStart / TimeHelper.dayInMillis).toInt()
    }

    fun daysOfEditionPassed(timestamp: Long): Int {
        if (timestamp < startDay0HourStamp) return 0
        val dayOfEditionToday = dayNumOf(TimeHelper.now())
        return if (dayOfEditionToday != -1) dayOfEditionToday else lengthInDays
    }

    fun dateByDayNum(dayNum: Int): String {
        val inMillis = startDay0HourStamp + (dayNum * TimeHelper.dayInMillis)
        return TimeHelper.getDate(inMillis)
    }

    fun dayOfWeekByDayNum(dayNum: Int): Int {
        val inMillis = startDay0HourStamp + (dayNum * TimeHelper.dayInMillis)
        return TimeHelper.dayOfWeekOf(inMillis)
    }
}