package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.reporter_activity.*
import pksokolowski.github.com.threegoals.EditionsManager
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Report

class ReporterActivity : AppCompatActivity() {
    companion object {
        private const val EXTRAS_EDITION_ID = "edition_id"
        private const val EXTRAS_EDITION_DAY_NUMBER = "edition_day_number"
        fun newIntent(context: Context, editionID: Long, editionDayNum: Int): Intent {
            return Intent(context, ReporterActivity::class.java).apply {
                putExtra(EXTRAS_EDITION_ID, editionID)
                putExtra(EXTRAS_EDITION_DAY_NUMBER, editionDayNum)
            }
        }
    }

    private var editingMode = false
    private var reportsToBeModified = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reporter_activity)

        val bundle = intent.extras

        showData(bundle)
    }

    private fun showData(bundle: Bundle?) {
        if (bundle == null) return
        val editionID = bundle.getLong(EXTRAS_EDITION_ID, -1)
        val dayNumber = bundle.getInt(EXTRAS_EDITION_DAY_NUMBER, -1)
        if (editionID == -1L || dayNumber == -1) return

        val edition = EditionsManager.getEditionById(this, editionID) ?: return
        val db = DbHelper.getInstance(this)
        val goals = db.getGoals(edition)
        reportsToBeModified = db.getReportsForDay(edition, dayNumber)

        val reportForms = getReportForms()
        if (reportsToBeModified.size > 0) {
            // modification of an existing report,
            editingMode = true
            for (i in reportForms.indices) {
                reportForms[i].setData(
                        i,
                        goals[i].name,
                        reportsToBeModified[i].score_trying_hard,
                        reportsToBeModified[i].score_positives)
            }
        } else {
            // in case of new day, not modification of existing
            for (i in reportForms.indices) {
                reportForms[i].setData(i, goals[i].name)
            }
        }
    }

    private fun getReportForms() = arrayOf(
            goal_1 as ReportFormFragment,
            goal_2 as ReportFormFragment,
            goal_3 as ReportFormFragment
    )
}