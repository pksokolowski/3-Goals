package pksokolowski.github.com.threegoals

import android.content.Context
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Edition
import java.util.*

object EditionsManager {
    private var editions = mutableListOf<Edition>()
    private var isReady: Boolean = false

    private fun setup(context: Context) {
        if (isReady) return
        getEditionsData(context)
        isReady = true
    }

    private fun getEditionsData(context: Context) {
        editions = DbHelper.getInstance(context).getEditions()
    }

    public fun createEdition(context: Context): Edition {
        setup(context)

        val now = Calendar.getInstance().timeInMillis
        val today0hour = TimeHelper.get0HourTimeOfAGivenDay(now)

        val edition = Edition(-1, generateTitle(now), 3, today0hour, 365)

        val id = DbHelper.getInstance(context).pushEdition(edition)
        val newEdition = Edition(id, edition.title, edition.goals_count, edition.start_day_timestamp, edition.length_in_days)
        editions.add(newEdition)
        return newEdition
    }

    public fun getEditionsCount(context: Context): Int {
        setup(context)
        return editions.size
    }

    public fun getCurrentEdition(context: Context): Edition? {
        setup(context)
        if (editions.size == 0) return null

        val latest = editions.get(editions.size - 1)
        val latest_endDay = latest.start_day_timestamp + (TimeHelper.dayInMillis * latest.length_in_days)
        val now = Calendar.getInstance().timeInMillis

        if (latest_endDay > now) return latest
        return null
    }

    private fun generateTitle(timeStamp: Long): String {
        return with(TimeHelper) {
            getYear(timeStamp) + "/" + getYearLast2Digits(timeStamp + yearInMillis)
        }
    }
}