package pksokolowski.github.com.threegoals

import android.content.Context
import pksokolowski.github.com.threegoals.alarms.AlarmsManager
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Goal
import java.util.*

object EditionsManager {
    private var editions = mutableListOf<Edition>()
    private var setupAlreadyInitiated: Boolean = false

    private fun setup(context: Context) {
        if (setupAlreadyInitiated) return
        setupAlreadyInitiated = true

        getEditionsData(context)
    }

    private fun getEditionsData(context: Context) {
        editions = DbHelper.getInstance(context).getEditions()
        // if there are no editions in existence, create
        // the first one:
        if(editions.isEmpty()) createEdition(context)
    }

    fun createEdition(context: Context): Edition {
        setup(context)

        // if there already is an ongoing edition, return it instead.
        val current = getCurrentEdition(context)
        if(current != null) return current

        val now = Calendar.getInstance().timeInMillis
        val today0hour = TimeHelper.get0HourTimeOfAGivenDay(now)

        val edition = Edition(-1, generateTitle(now), 3, today0hour, 365)

        val db = DbHelper.getInstance(context)
        val id = db.pushEdition(edition)

        // create as many goals as defined by the edition
        val romanNums = arrayOf("Ⅰ", "Ⅱ", "Ⅲ", "?")
        for (i in 0 until edition.goals_count) {
            val initial = romanNums[Math.min(romanNums.size - 1, i)]
            db.pushGoal(Goal(-1, "", initial, i, id))
        }

        val newEdition = Edition(id, edition.title, edition.goals_count, edition.start_day_timestamp, edition.length_in_days)
        editions.add(newEdition)
        AlarmsManager.setupAlarms(context)
        return newEdition
    }

    public fun getEditionsCount(context: Context): Int {
        setup(context)
        return editions.size
    }

    public fun getCurrentEdition(context: Context): Edition? {
        setup(context)

        if(editions.size == 0) return null
        val latest = editions.last()
        val latestEndDay = latest.start_day_timestamp + (TimeHelper.dayInMillis * latest.length_in_days)
        val now = Calendar.getInstance().timeInMillis

        if (latestEndDay > now) return latest
        return null
    }

    public fun getEditionById(context: Context, id: Long): Edition? {
        setup(context)
        for (e in editions) {
            if (e.ID == id) return e
        }
        return null
    }

    private fun generateTitle(timeStamp: Long): String {
        return with(TimeHelper) {
            getYear(timeStamp) + "/" + getYearLast2Digits(timeStamp + yearInMillis)
        }
    }

    fun getEditionsTitles(context: Context): Array<String> {
        setup(context)
        return Array(editions.size) { editions[it].title }
    }

    fun getAllEditions(context: Context): Array<Edition>{
        setup(context)
        return Array(editions.size) { editions[it]}
    }
}