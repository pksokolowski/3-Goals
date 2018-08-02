package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.Toast
import kotlinx.android.synthetic.main.reporter_activity.*
import pksokolowski.github.com.threegoals.EditionsManager
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.TimeHelper
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Goal
import pksokolowski.github.com.threegoals.models.Report
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

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

    private var mReportsToBeModified = mutableListOf<Report>()
    private var mGoals = mutableListOf<Goal>()
    private lateinit var mEdition: Edition
    private var mDayNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reporter_activity)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        obtainDataFromExtras(intent.extras)
        showData()
        // block saving if there is a problem with data provided
        // most likely cause is not-using the newIntent() method
        // and forging and intent manually, without the required
        // extras.
        lockSavingIfDataIsMissing()

        done_button.setOnClickListener {
            if (!isInputValid()) return@setOnClickListener
            val forms = getReportForms()
            val db = DbHelper.getInstance(this)

            val reports = mutableListOf<Report>()
            if (mReportsToBeModified.size == 0) {
                saveBrandNewReports(forms, reports)
            } else {
                editExistingReports(forms, db)
            }
        }

        reporter_constraint_layout.setOnApplyWindowInsetsListener insetsListener@{ _, windowInsets ->
            reporter_constraint_layout.setPadding(0, 0, 0,
                windowInsets.systemWindowInsetBottom)
            return@insetsListener windowInsets.consumeSystemWindowInsets()
        }
    }

    private fun editExistingReports(forms: Array<ReportFormFragment>, db: DbHelper) {
        // editing existing reports
        // todo: implement updating report rows
        for (i: Int in forms.indices) {
            val rep = mReportsToBeModified[i]
            val f = forms[i]
            // update custom goal name if needed
            updateCustomGoalName(i, f, db)

            db.updateReport(Report(
                    rep.ID,
                    rep.day_num,
                    rep.time_stamp,
                    f.getTryingHardScore(),
                    f.getPositivesCount(),
                    rep.goal)
            )

        }
        Toast.makeText(this,
                getString(R.string.reporter_activity_message_reports_updated),
                Toast.LENGTH_LONG).show()
        finish()
    }

    private fun saveBrandNewReports(forms: Array<ReportFormFragment>, reports: MutableList<Report>) {
        // brand new report, no editing of existing reports
        val db = DbHelper.getInstance(this)

        val now = TimeHelper.now()
        for (i: Int in forms.indices) {
            val f = forms[i]
            // update custom goal name if needed
            updateCustomGoalName(i, f, db)

            reports.add(Report(
                    -1,
                    mDayNumber,
                    now,
                    f.getTryingHardScore(),
                    f.getPositivesCount(),
                    mGoals[i].ID)
            )

        }

        // save reports
        db.pushReports(reports, mEdition)

        NotificationsManager.cancelNotification(this)

        Toast.makeText(this,
                getString(R.string.reporter_activity_message_reports_saved),
                Toast.LENGTH_LONG).show()

        finish()
    }

    private fun updateCustomGoalName(i: Int, f: ReportFormFragment, db: DbHelper) {
        if (mGoals[i].name != f.getCustomName()) {
            db.updateGoalCustomName(mGoals[i], f.getCustomName())
        }
    }

    private fun isInputValid(): Boolean {
        val forms = getReportForms()
        for (f in forms) {
            if (f.getTryingHardScore() == -1) {
                Toast.makeText(this,
                        getString(R.string.reporter_activity_error_select_values),
                        Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun obtainDataFromExtras(bundle: Bundle?) {
        if (bundle == null) return
        val editionID = bundle.getLong(EXTRAS_EDITION_ID, -1)
        mDayNumber = bundle.getInt(EXTRAS_EDITION_DAY_NUMBER, -1)
        if (editionID == -1L || mDayNumber == -1) return

        mEdition = EditionsManager.getEditionById(this, editionID) ?: return
        val db = DbHelper.getInstance(this)
        mGoals = db.getGoals(mEdition)
        mReportsToBeModified = db.getReportsForDay(mEdition, mDayNumber)
    }

    private fun showData(){
        date_textview.text = mEdition.dateByDayNum(mDayNumber)
        val reportForms = getReportForms()
        if (mReportsToBeModified.size > 0) {
            // modification of an existing report,
            for (i in reportForms.indices) {
                reportForms[i].setData(
                        i,
                        mGoals[i].name,
                        mReportsToBeModified[i].score_trying_hard,
                        mReportsToBeModified[i].score_positives)
            }
        } else {
            // in case of new day, not modification of existing
            for (i in reportForms.indices) {
                reportForms[i].setData(i, mGoals[i].name)
            }
        }
    }

    private fun lockSavingIfDataIsMissing(){
        if(mDayNumber == -1 || !this::mEdition.isInitialized){
            done_button.isEnabled = false
        }
    }

    private fun getReportForms() = arrayOf(
            goal_1 as ReportFormFragment,
            goal_2 as ReportFormFragment,
            goal_3 as ReportFormFragment
    )

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}