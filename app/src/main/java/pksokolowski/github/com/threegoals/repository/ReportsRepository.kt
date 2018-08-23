package pksokolowski.github.com.threegoals.repository

import android.arch.lifecycle.MediatorLiveData
import pksokolowski.github.com.threegoals.data.ReportsDao
import pksokolowski.github.com.threegoals.model.Day
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Report
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsRepository @Inject constructor(private val reportsDao: ReportsDao) {

//    private val reportsData = reportsDao.getReports()
//    private val daysData = MediatorLiveData<DaysData>()
//    init {
//        daysData.addSource()
//    }

    fun getReportsForDay(edition: Edition, dayNum: Int) = reportsDao.getReportsForDay(edition.id, dayNum)

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
}