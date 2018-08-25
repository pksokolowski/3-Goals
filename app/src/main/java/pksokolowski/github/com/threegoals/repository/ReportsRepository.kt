package pksokolowski.github.com.threegoals.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import pksokolowski.github.com.threegoals.data.GoalsDao
import pksokolowski.github.com.threegoals.data.ReportsDao
import pksokolowski.github.com.threegoals.model.Day
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Report
import pksokolowski.github.com.threegoals.utils.ReportsValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsRepository @Inject constructor(private val reportsDao: ReportsDao, private val goalsDao: GoalsDao, editionsRepository: EditionsRepository, private val validator: ReportsValidator) {

//    private var editionSelected = editionsRepository.getLatestEdition()

    // do not expose this property to the outside world, the reference will be dropped
    // observing it makes less than perfect sense even though it might behave
    // as expected in most situations.
    private var temporaryReportsSource = reportsDao.getReports(
            // this can be replaced with code that does not try to guess the edition
            // to use, and instead relies solely on what is provided when asking for the data.
            editionsRepository.getLatestEdition().id
    )

    private val daysData = MediatorLiveData<DaysData>()

    fun getDaysData(edition: Edition): LiveData<DaysData> {
        val data = daysData.value
        if (data == null || data.edition.id != edition.id) {
            fetchData(edition)
        }

        return daysData
    }

    fun getReportsForDay(edition: Edition, dayNum: Int): MutableList<Report> {
        return reportsDao.getReportsForDay(edition.id, dayNum)
    }

    private fun fetchData(edition: Edition) {
        daysData.removeSource(temporaryReportsSource)
        temporaryReportsSource = reportsDao.getReports(edition.id)
        daysData.addSource(temporaryReportsSource) {
            val reports = it ?: mutableListOf()
            val days = getDays(edition, reports)
            val goals = goalsDao.getGoals(edition.id)

            daysData.value = DaysData(edition, goals, days)
        }
    }

    private fun getDays(edition: Edition, reports: MutableList<Report>): Array<Day?> {
        val days = arrayOfNulls<Day>(edition.lengthInDays)
        val daysInProgress = arrayOfNulls<MutableList<Report>>(edition.lengthInDays)
        // group reports by day
        for (report in reports) {
            if (daysInProgress[report.dayNum] == null) {
                // create day in progress list
                daysInProgress[report.dayNum] = mutableListOf()
            }
            daysInProgress[report.dayNum]!!.add(report)
        }

        // create days
        for (i in daysInProgress.indices) {
            val daysReports = daysInProgress[i]
            if (daysReports != null) {
                days[i] = Day(i, daysReports.toTypedArray())
            }
        }

        return days
    }

    fun insertReports(reports: List<Report>) {
        validator.validateReportsBatch(reports)
        reportsDao.insertReports(reports)
    }

    fun updateReports(reports: List<Report>) {
        validator.validateReportsBatch(reports, true)
        reportsDao.updateReports(reports)
    }
}