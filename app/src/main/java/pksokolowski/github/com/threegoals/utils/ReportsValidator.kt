package pksokolowski.github.com.threegoals.utils

import pksokolowski.github.com.threegoals.data.EditionsDao
import pksokolowski.github.com.threegoals.data.GoalsDao
import pksokolowski.github.com.threegoals.data.ReportsDao
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Report
import pksokolowski.github.com.threegoals.repository.GoalsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject

class ReportsValidator @Inject constructor(private val goalsDao: GoalsDao, private val reportsDao: ReportsDao, private val editionsDao: EditionsDao) {
    fun isReportsBatchValid(reports: List<Report>): Boolean {
        return try {
            validateReportsBatch(reports)
            true
        } catch (e: RuntimeException) {
            false
        }
    }

    fun validateReportsBatch(reports: List<Report>) {
        // reject empty list
        if(reports.isEmpty()) throw RuntimeException("Attempted to save an empty list of reports")

        // establish edition based on first report in the batch
        val goal = goalsDao.getGoalById(reports[0].goal)
        val edition = editionsDao.getEditionById(goal.edition)

        // validate the number of reports - must match number of reports expected given the edition
        if (reports.size != edition.goalsCount) throw RuntimeException("Attempted to save an incorrect number of reports.")

        // validate that reports timeStamps are later than the edition's start day timeStamp.
        // Note: This won't necessarily work for the end time of the edition, as reports may
        // be provided later than the edition end and it's not only valid but expected.
        for (report in reports) {
            if (report.timeStamp < edition.startDay0HourStamp) throw RuntimeException("Attempting to save a report with timeStamp earlier than the edition start day.")
        }

        // validate that all reports are for the same day
        val expectedDayNum = reports[0].dayNum
        for (report in reports) {
            if (report.dayNum != expectedDayNum) throw RuntimeException("Attempted to save reports for different days, in the same batch.")
        }

        // validate that each goal only appears once
        val uniqueGoals = HashSet<Long>(reports.size)
        for (report in reports) {
            val goal = report.goal
            if (uniqueGoals.contains(goal)) throw RuntimeException("Attempted to save reports batch with duplicate goal IDs")
            uniqueGoals.add(goal)
        }

        // validate that goals of all of the reports exist in the edition
        val editionsGoals = goalsDao.getGoals(edition.id)
        for (i in editionsGoals.indices) {
            if (reports[i].goal != editionsGoals[i].id) throw RuntimeException("Attempted to save reports with a mismatch: Goals not matching edition.")
        }

        // validate that the reports are not already present for the day
        val existingReportsForTheSameDay = reportsDao.getReportsForDay(edition.id, expectedDayNum)
        if (existingReportsForTheSameDay.size > 0) throw RuntimeException("Attempted to save duplicate reports for a day, which already had reports saved.")
    }
}