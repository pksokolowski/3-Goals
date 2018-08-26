package pksokolowski.github.com.threegoals.reporter

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.utils.TimeHelper
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Goal
import pksokolowski.github.com.threegoals.model.Report
import pksokolowski.github.com.threegoals.notifications.NotificationsManager
import pksokolowski.github.com.threegoals.repository.EditionsRepository
import pksokolowski.github.com.threegoals.repository.GoalsRepository
import pksokolowski.github.com.threegoals.repository.ReportsRepository
import javax.inject.Inject

class ReporterActivityViewModel @Inject constructor(private val reportsRepository: ReportsRepository, private val goalsRepository: GoalsRepository, private val editionsRepository: EditionsRepository, private val notificationsManager: NotificationsManager, private val context: Application) : ViewModel() {

    fun cancelNotification() {
        notificationsManager.cancelNotification()
    }

    private var mReportsToBeModified = mutableListOf<Report>()
    private var mGoals = mutableListOf<Goal>()
    private lateinit var mEdition: Edition
    private var mDayNumber: Int = -1


    fun saveBrandNewReports(forms: Array<ReportFormFragment>) {
        val reports = mutableListOf<Report>()
        val now = TimeHelper.now()
        for (i: Int in forms.indices) {
            val f = forms[i]
            // update custom goal name if needed
            updateCustomGoalName(mGoals[i], f)

            reports.add(Report(
                    0,
                    mDayNumber,
                    now,
                    f.getTryingHardScore(),
                    f.getPositivesCount(),
                    mGoals[i].id)
            )

        }

        // save reports
        reportsRepository.insertReports(reports)

        cancelNotification()

        Toast.makeText(context,
                context.getString(R.string.reporter_activity_message_reports_saved),
                Toast.LENGTH_LONG).show()

    }

    fun editExistingReports(forms: Array<ReportFormFragment>) {
        // editing existing reports
        val modifiedReports = MutableList(mReportsToBeModified.size) { i ->
            val rep = mReportsToBeModified[i]
            val f = forms[i]
            // update custom goal name if needed
            updateCustomGoalName(mGoals[i], f)

            Report(
                    rep.id,
                    rep.dayNum,
                    rep.timeStamp,
                    f.getTryingHardScore(),
                    f.getPositivesCount(),
                    rep.goal
            )
        }
        reportsRepository.updateReports(modifiedReports)

        Toast.makeText(context,
                context.getString(R.string.reporter_activity_message_reports_updated),
                Toast.LENGTH_LONG).show()
    }

    fun updateCustomGoalName(goal: Goal, f: ReportFormFragment) {
        val newName = f.getCustomName()
        if (goal.name != newName) {
            val goalReplacement = goal.changeCustomName(newName)
            goalsRepository.updateGoal(goalReplacement)
        }
    }

    fun fillReportFormsWithData(reportForms: Array<ReportFormFragment>) {
        if (mReportsToBeModified.size > 0) {
            // modification of an existing report,
            for (i in reportForms.indices) {
                reportForms[i].setData(
                        i,
                        mGoals[i].name,
                        mReportsToBeModified[i].scoreTryingHard,
                        mReportsToBeModified[i].scorePositives)
            }
        } else {
            // in case of new day, not modification of existing
            for (i in reportForms.indices) {
                reportForms[i].setData(i, mGoals[i].name)
            }
        }
    }

    fun getNumOfReportsToModify() = mReportsToBeModified.size

    fun getDate() = mEdition.dateByDayNum(mDayNumber)

    fun loadData(editionID: Long, dayNum: Int): Int {
        mDayNumber = dayNum
        if (editionID == -1L || mDayNumber == -1) return DATA_LOAD_FAILURE

        mEdition = editionsRepository.getEditionById(editionID) ?: return DATA_LOAD_FAILURE
        mGoals = goalsRepository.getData(mEdition)
        mReportsToBeModified = reportsRepository.getReportsForDay(mEdition, mDayNumber)

        return DATA_LOAD_SUCCESS
    }

    companion object {
        const val DATA_LOAD_SUCCESS = 0
        const val DATA_LOAD_FAILURE = 1
    }
}