package pksokolowski.github.com.threegoals.repository

import android.app.Application
import pksokolowski.github.com.threegoals.alarms.BootFinishedReceiver
import pksokolowski.github.com.threegoals.utils.TimeHelper
import pksokolowski.github.com.threegoals.alarms.AlarmsManager
import pksokolowski.github.com.threegoals.data.EditionsDao
import pksokolowski.github.com.threegoals.data.GoalsDao
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Goal
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditionsRepository @Inject constructor(private val editionsDao: EditionsDao, private val goalsDao: GoalsDao, private val appContext: Application) {
    private val editions = editionsDao.getEditions()

    init {
        if (editions.isEmpty()) {
            // create the first edition immediately
            createEdition()
        }
    }

    fun createEdition(): Edition {

        // if there already is an ongoing edition, return it instead.
        val current = getCurrentEdition()
        if (current != null) return current

        val now = Calendar.getInstance().timeInMillis
        val today0hour = TimeHelper.get0HourTimeOfAGivenDay(now)

        val edition = Edition(0, generateTitle(now), 3, today0hour, 365)
        val id = editionsDao.insertEdition(edition)

        // create as many goals as defined by the edition
        val romanNums = arrayOf("Ⅰ", "Ⅱ", "Ⅲ", "?")
        val goals = MutableList(edition.goalsCount) {
            val initial = romanNums[Math.min(romanNums.size - 1, it)]
            Goal(0, "", initial, it, id)
        }
        goalsDao.insertGoals(goals)

        val newEdition = Edition(id, edition.title, edition.goalsCount, edition.startDay0HourStamp, edition.lengthInDays)
        AlarmsManager.setupAlarms(appContext)
        BootFinishedReceiver.setBootFinishedReceiverEnabled(appContext, true)

        editions.add(newEdition)
        return newEdition
    }

    fun getCurrentEdition(): Edition? {
        if (editions.isEmpty()) return null
        val latest = editions.last()
        val latestEndDay = latest.startDay0HourStamp + (TimeHelper.dayInMillis * latest.lengthInDays)
        val now = Calendar.getInstance().timeInMillis

        if (latestEndDay > now) return latest
        return null
    }

    private fun generateTitle(timeStamp: Long): String {
        return with(TimeHelper) {
            getYear(timeStamp) + "/" + getYearLast2Digits(timeStamp + yearInMillis)
        }
    }

    fun getLatestEdition(): Edition {
        require(editions.isNotEmpty())

        return editions.last()
    }

    fun getEditionById(id: Long): Edition? {
        for (e in editions) {
            if (e.id == id) return e
        }
        return null
    }

    fun getAllEditions(): Array<Edition> {
        return Array(editions.size) { editions[it] }
    }

    fun getAllEditionsAsLiveData() = editionsDao.getEditionsAsLiveData()
}